package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import scala.concurrent.Future;

import java.util.concurrent.*;

import static akka.pattern.Patterns.ask;
import static org.junit.Assert.*;
import static scala.compat.java8.FutureConverters.toJava;

public class StringReverseActorTest {

    ActorSystem system = ActorSystem.create();
    ActorRef actorRef = system.actorOf(Props.create(StringReverseActor.class));

    @Test
    public void shouldReverseString() throws Exception {
        Future sFuture = ask(actorRef, "OriginalString", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture<String>)cs;
        assertEquals(StringUtils.reverse("OriginalString"), jFuture.get(1000, TimeUnit.MILLISECONDS));
    }

}