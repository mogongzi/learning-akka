package me.ryan.learnakka;

import akka.actor.*;
import akka.util.Timeout;
import me.ryan.learnakka.message.SetRequest;

import java.util.concurrent.TimeoutException;

public class TellDemoArticleParser extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection articleParseActor;
    private final Timeout timeout;

    public TellDemoArticleParser(ActorSelection cacheActor, ActorSelection httpClientActor, ActorSelection articleParseActor, Timeout timeout) {
        this.cacheActor = cacheActor;
        this.httpClientActor = httpClientActor;
        this.articleParseActor = articleParseActor;
        this.timeout = timeout;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match();
    }

    private ActorRef buildExtraActor(ActorRef senderRef, String uri) {
        class MyActor extends AbstractActor {

            @Override
            public Receive createReceive() {
                return receiveBuilder()
                        .matchEquals(String.class, x -> x.equals("timeout"), x -> {
                            senderRef.tell(new Status.Failure(new TimeoutException("timeout!")), self());
                            context().stop(self());
                        })
                        .match(HttpResponse.class, httpResponse -> {
                            articleParseActor.tell(new ParseHtmlArticle(uri, httpResponse.getBody()), self());
                        })
                        .match(String.class, body -> {
                            senderRef.tell(body, self());
                            context().stop(self());
                        })
                        .match(ArticleBody.class, articleBody -> {
                            cacheActor.tell(new SetRequest(articleBody.getUri(), articleBody.getBody()), self());
                            context().stop(self());
                            senderRef.tell(articleBody.getBody(), self());
                        })
                        .matchAny(t -> {
                            System.out.println("ignoring message: " + t.getClass());
                        })
                        .build();
            }
        }

        return context().actorOf(Props.create(MyActor.class, MyActor::new));
    }
}
