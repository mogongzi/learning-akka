package me.ryan.learnakka;

import akka.actor.AbstractActor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ParsingActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseHtmlArticle.class, msg -> {
                    String body = ArticleExtractor.INSTANCE.getText(msg.getHtml());
                    sender().tell(new ArticleBody(msg.getUri(), body), self());
                })
                .build();
    }
}
