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
import static org.junit.Assert.assertEquals;

public class PingActorTest {

    ActorSystem system = ActorSystem.create();
    ActorRef actorRef = system.actorOf(Props.create(PingActor.class));

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
    public void shouldPrintToConsole() throws Exception {
        askPong("Ping")
                .thenAccept(x -> System.out.println("replied with: " + x));
        Thread.sleep(1000);
    }

    @Test
    public void shouldTransform() throws Exception {
        char result = (char) get(askPong("Ping").thenApply(x -> x.charAt(0)));
        assertEquals('P', result);
    }

    @Test
    public void shouldTransformAsync() throws Exception {
        CompletionStage cs = askPong("Ping").thenCompose(x -> askPong("Ping"));
        assertEquals(get(cs), "Pong");
    }

    @Test
    public void shouldEffectOnError() {
        askPong("cause error").handle((x, t) -> {
            if (t != null) {
                System.out.println("Error:" + t);
            }
            return null;
        });
    }

    @Test
    public void shouldRecoverOnErrorsAsync() throws Exception {
        CompletionStage<String> cf = askPong("cause error")
                .handle((pong, ex) -> ex == null ? CompletableFuture.completedFuture(pong) : askPong("Ping"))
                .thenCompose(x -> x);
        assertEquals("Pong", get(cf));
    }

    @Test
    public void shouldChainTogetherMultipleOperations() {
        askPong("Ping").thenCompose(x -> askPong("Ping" + x)).handle((x, t) ->
                t != null ? "default" : x);
    }

    @Test
    public void shouldPrintErrorToConsole() throws Exception {
        askPong("cause error").handle((x, t) -> {
            if (t != null) {
                System.out.println("Error: " + t);
            }
            return null;
        });
        Thread.sleep(1000L);
    }

    public Object get(CompletionStage cs) throws Exception {
        return ((CompletableFuture<String>) cs).get(1000, TimeUnit.MILLISECONDS);
    }

    public CompletionStage<String> askPong(String message) {
        Future sFuture = ask(actorRef, "Ping", 1000);
        CompletionStage<String> cs = FutureConverters.asJava(sFuture);
        return cs;
    }
}