package me.ryan.learnakka.logging;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ApplicationMain {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        ActorRef pingActor = system.actorOf(LoggingActor.props(), "pingActor");
        pingActor.tell("message1", null);
        Thread.sleep(2000L);
        pingActor.tell("message2", null);
        Thread.sleep(2000L);
        pingActor.tell("message3", null);
        Thread.sleep(2000L);
        system.terminate();
    }
}
