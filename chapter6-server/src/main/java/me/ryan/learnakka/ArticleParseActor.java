package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.Status;

public class ArticleParseActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseArticle.class, html -> {
                    System.out.println(ArticleParser.apply(html.getHtmlBody()).orElse("empty?"));

                    ArticleParser.apply(html.getHtmlBody())
                            .onSuccess(body -> sender().tell(body, self()))
                            .onFailure(t -> sender().tell(new Status.Failure(t), self()));
                })
                .matchAny(x -> System.out.println("GOT A MSG!!!" + x))
                .build();
    }
}
