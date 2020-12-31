package me.ryan.learnakka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import com.typesafe.config.ConfigFactory;
import me.ryan.learnakka.message.GetRequest;
import me.ryan.learnakka.message.SetRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HotswapClientActorTest {

    ActorSystem system = ActorSystem.create("testSystem", ConfigFactory.defaultReference());

    @Test
    public void itShouldSet() {
        TestActorRef<AkkaKVDbActor> dbRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));
        AkkaKVDbActor dbActor = dbRef.underlyingActor();

        TestProbe probe = TestProbe.apply(system);
        TestActorRef<HotswapClientActor> clientRef = TestActorRef.create(system, Props.create(HotswapClientActor.class, dbRef.path().toString()));

        clientRef.tell(new SetRequest("testkey", "testvalue", probe.ref()), probe.ref());

        probe.expectMsg(new Status.Success("testkey"));
        assertEquals("testvalue", dbActor.map.get("testkey"));
    }

    @Test
    public void itShouldGet() {
        TestActorRef<AkkaKVDbActor> dbRef = TestActorRef.create(system, Props.create(AkkaKVDbActor.class));
        AkkaKVDbActor dbActor = dbRef.underlyingActor();
        dbActor.map.put("testkey", "testvalue");

        TestProbe probe = TestProbe.apply(system);
        TestActorRef<HotswapClientActor> clientRef = TestActorRef.create(system, Props.create(HotswapClientActor.class, dbRef.path().toString()));

        clientRef.tell(new GetRequest("testkey"), probe.ref());
        probe.expectMsg("testvalue");
    }

}