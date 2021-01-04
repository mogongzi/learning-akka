package me.ryan.learnakka;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestCameoActor extends AbstractActor {

    private final CompletableFuture futureToComplete;
    private List<String> articles = new ArrayList<>();

    public TestCameoActor(CompletableFuture futureToComplete) {
        this.futureToComplete = futureToComplete;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, x -> {
                    articles.add(x);
                    if (articles.size() == 2000) {
                        futureToComplete.complete("OK!");
                    }
                })
                .build();
    }
}
