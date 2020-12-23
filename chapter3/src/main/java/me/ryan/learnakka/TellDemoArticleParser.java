package me.ryan.learnakka;

import akka.actor.*;
import akka.util.Timeout;
import me.ryan.learnakka.message.GetRequest;
import me.ryan.learnakka.message.SetRequest;

import java.util.concurrent.TimeoutException;

public class TellDemoArticleParser extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection articleParseActor;
    private final Timeout timeout;

    public TellDemoArticleParser(String cacheActorPath, String httpClientActorPath, String articleParseActorPath, Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor = context().actorSelection(httpClientActorPath);
        this.articleParseActor = context().actorSelection(articleParseActorPath);
        this.timeout = timeout;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseArticle.class, message -> {
                    ActorRef extraActor = buildExtraActor(sender(), message.getUrl());
                    cacheActor.tell(new GetRequest(message.getUrl()), extraActor);
                    httpClientActor.tell(message.getUrl(), extraActor);

                    context().system().scheduler().scheduleOnce(timeout.duration(), extraActor, "timeout",
                            context().system().dispatcher(), ActorRef.noSender());
                })
                .build();
    }

    private ActorRef buildExtraActor(ActorRef senderRef, String uri) {
        class MyActor extends AbstractActor {

            @Override
            public Receive createReceive() {
                return receiveBuilder()
                        .matchEquals("timeout", x -> {
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
