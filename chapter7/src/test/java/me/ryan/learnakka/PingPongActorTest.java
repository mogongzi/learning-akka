package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

            final String pingResult = expectMsgPF("ping message", in -> {
                if (in instanceof PingActor.PingMessage) {
                    return ((PingActor.PingMessage) in).getText();
                }
                return "";
            });

            assertEquals("ping", pingResult);
        }};
    }

    @Test
    public void testPongActor() {
        new TestKit(system) {{
            final ActorRef pongActor = system.actorOf(PongActor.props());
            pongActor.tell(new PingActor.PingMessage("ping"), getRef());

            final String pongResult = expectMsgPF("pong message", in -> {
                if (in instanceof PongActor.PongMessage) {
                    return ((PongActor.PongMessage) in).getText();
                }
                return "";
            });

            assertEquals("pong", pongResult);
        }};
    }

}