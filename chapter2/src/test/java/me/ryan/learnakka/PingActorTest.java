package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.Test;
import scala.concurrent.Future;
import scala.jdk.javaapi.FutureConverters;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.junit.Assert.*;

public class PingActorTest {

    ActorSystem system = ActorSystem.create();
    ActorRef actorRef = system.actorOf(Props.create(PingActor.class));

    public CompletionStage<String> askPong(String message) {
        Future sFuture = ask(actorRef, "Ping", 1000);
        CompletionStage<String> cs = FutureConverters.asJava(sFuture);
        return cs;
    }

    @Test
    public void shouldReplyToPingWithPong() throws Exception {
        Future sFuture = ask(actorRef, "Ping", 1000);
        final CompletionStage<String> cs = FutureConverters.asJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
        assertEquals("Pong", jFuture.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        Future sFuture = ask(actorRef, "unknown", 1000);
        final CompletionStage<String> cs = FutureConverters.asJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
        jFuture.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void printToConsole() throws Exception {
        askPong("Ping")
                .thenAccept(x -> System.out.println("replied with: "  + x));
        Thread.sleep(100);
    }
}