package me.ryan.learnakka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import me.ryan.learnakka.message.GetRequest;
import me.ryan.learnakka.message.SetRequest;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

public class FSMClientActorTest {

    ActorSystem system = ActorSystem.create("testSystem", ConfigFactory.defaultReference());

    TestActorRef<AkkaKVDbActor> dbRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));

    TestProbe probe = TestProbe.apply(system);

    @Test
    public void itShouldTransitionToConnectedAndPending() {
        TestActorRef<FSMClientActor> fsmClientRef = TestActorRef.create(system, Props.create(FSMClientActor.class, dbRef.path().toString()));

        assertEquals(State.DISCONNECTED, fsmClientRef.underlyingActor().stateName());
        fsmClientRef.tell(new GetRequest("testkey"), probe.ref());
        assertEquals(State.CONNECTED_AND_PENDING, fsmClientRef.underlyingActor().stateName());
    }

    @Test
    public void itShouldFlushMessagesInConnectedAndPending() {
        TestActorRef<FSMClientActor> fsmClientRef = TestActorRef.create(system, Props.create(FSMClientActor.class, dbRef.path().toString()));

        fsmClientRef.tell(new SetRequest("testkey", "testvalue", probe.ref()), probe.ref());
        assertEquals(State.CONNECTED_AND_PENDING, fsmClientRef.underlyingActor().stateName());

        fsmClientRef.tell(new FlushMessage(), probe.ref());
        probe.expectMsgClass(Status.Success.class);
        assertEquals(State.CONNECTED, fsmClientRef.underlyingActor().stateName());
    }
}