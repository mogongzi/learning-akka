package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.concurrent.Future;
import scala.jdk.javaapi.FutureConverters;

import java.util.concurrent.*;

import static akka.pattern.Patterns.ask;

public class StringReverseService {

    ActorSystem system = ActorSystem.create();
    ActorRef actorRef = system.actorOf(Props.create(StringReverseActor.class));

    public String get(String original) throws Exception {
        Future sFuture = ask(actorRef, original, 1000);
        final CompletionStage<String> cs = FutureConverters.asJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
        return jFuture.get(1000, TimeUnit.MILLISECONDS);
    }
}
