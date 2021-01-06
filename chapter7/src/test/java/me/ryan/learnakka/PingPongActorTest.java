package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PingPongActorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testPingActor() {
        new TestKit(system) {{
          final ActorRef pingActor = system.actorOf(PingActor.props());
          pingActor.tell(new PongActor.PongMessage("pong"), getRef());

          expectMsgClass(PingActor.PingMessage.class);
        }};
    }

    @Test
    public void testPongActor() {
        new TestKit(system) {{
           final ActorRef pongActor = system.actorOf(PongActor.props());
           pongActor.tell(new PingActor.PingMessage("ping"), getRef());

           expectMsgClass(PongActor.PongMessage.class);
        }};
    }

}