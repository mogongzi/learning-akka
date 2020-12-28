package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import com.typesafe.config.ConfigFactory;
import me.ryan.learnakka.message.SetRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AkkaKVDbActorTest {

    ActorSystem system = ActorSystem.create("system", ConfigFactory.empty());

    @Test
    public void itShouldPlaceKeyValueFromSetMessageIntoMap() {
        TestProbe testProbe = TestProbe.apply(system);
        TestActorRef<AkkaKVDbActor> actorRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));
        AkkaKVDbActor actor = actorRef.underlyingActor();

        actorRef.tell(new SetRequest("key", "value", testProbe.ref()), ActorRef.noSender());

        assertEquals("value", actor.map.get("key"));
    }

    @Test
    public void itShouldPlaceKeyValuesFromListOfSetMessageIntoMap() {
        TestProbe testProbe = TestProbe.apply(system);
        TestActorRef<AkkaKVDbActor> actorRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));
        AkkaKVDbActor actor = actorRef.underlyingActor();

        List<SetRequest> messages = Arrays.asList(
                new SetRequest("key1", "value1", testProbe.ref()),
                new SetRequest("key2", "value2", testProbe.ref())
        );

        actorRef.tell(messages, ActorRef.noSender());

        assertEquals("value1", actor.map.get("key1"));
        assertEquals("value2", actor.map.get("key2"));

        testProbe.expectMsg(new Status.Success("key1"));
        testProbe.expectMsg(new Status.Success("key2"));
    }
}