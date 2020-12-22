package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Status;
import akka.util.Timeout;
import me.ryan.learnakka.message.GetRequest;
import scala.jdk.javaapi.FutureConverters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

public class AskDemoArticleParser extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection articleParseActor;
    private final Timeout timeout;

    public AskDemoArticleParser(String cacheActorPath, String httpClientActorPath, String articleParseActorPath, Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor = context().actorSelection(httpClientActorPath);
        this.articleParseActor = context().actorSelection(articleParseActorPath);
        this.timeout = timeout;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseArticle.class, msg -> {
                    final CompletionStage cacheResult = FutureConverters.asJava(ask(cacheActor, new GetRequest(msg.getUrl()), timeout));
                    final CompletionStage result = cacheResult.handle((x, t) -> {
                        return (x != null) ?
                                CompletableFuture.completedFuture(x)
                                : FutureConverters.asJava(ask(httpClientActor, msg.getUrl(), timeout))
                                .thenCompose(rawArticle -> FutureConverters.asJava(ask(articleParseActor, new ParseHtmlArticle(msg.getUrl(), ((HttpResponse) rawArticle).getBody()), timeout)));
                    }).thenCompose(x -> x);

                    final ActorRef senderRef = sender();
                    result.handle((x, t) -> {
                        if (x != null) {
                            if (x instanceof ArticleBody) {
                                String body = ((ArticleBody) x).getBody();
                                cacheActor.tell(body, self());
                                senderRef.tell(body, self());
                            } else if (x instanceof String) {
                                senderRef.tell(x, self());
                            }
                        } else if (x == null) {
                            senderRef.tell(new Status.Failure((Throwable) t), self());
                        }
                        return null;
                    });
                }).build();
    }
}
