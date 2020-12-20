package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import me.ryan.learnakka.AkkaKVDbActor;
import me.ryan.learnakka.message.SetRequest;
import org.junit.Test;

import static org.junit.Assert.*;

public class AkkaKVDbActorTest {

    ActorSystem system = ActorSystem.create();

    @Test
    public void shouldPlaceKeyValueFromSetMessageIntoMap() {
        TestActorRef<AkkaKVDbActor> actorRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));
        actorRef.tell(new SetRequest("key1", "value1"), ActorRef.noSender());
        AkkaKVDbActor actor = actorRef.underlyingActor();
        assertEquals(actor.map.get("key1"), "value1");
    }
}