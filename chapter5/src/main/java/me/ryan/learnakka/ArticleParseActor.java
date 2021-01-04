package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.Status;

public class ArticleParseActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseArticle.class, x -> {
                    ArticleParser.apply(x.getHtmlBody())
                            .onSuccess(body -> sender().tell(body, self()))
                            .onFailure(t -> sender().tell(new Status.Failure(t), self()));
                })
                .build();
    }
}
