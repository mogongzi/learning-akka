package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MsgActorTest {

    ActorSystem system = ActorSystem.create();

    @Test
    public void shouldAlwaysReturnReceivedMessage() {
        TestActorRef<MsgActor> actorRef = TestActorRef.create(system, Props.create(MsgActor.class));
        actorRef.tell("hello", ActorRef.noSender());
        MsgActor actor= actorRef.underlyingActor();
        assertEquals("hello", actor.savedMsg);
    }

    @Test
    public void shouldAlwaysReturnTheLastReceivedMessage() throws InterruptedException {
        TestActorRef<MsgActor> actorRef = TestActorRef.create(system, Props.create(MsgActor.class));
        actorRef.tell("first", ActorRef.noSender());
        Thread.sleep(1000L);
        actorRef.tell("second", ActorRef.noSender());
        MsgActor actor= actorRef.underlyingActor();
        assertEquals("second", actor.savedMsg);
    }
}