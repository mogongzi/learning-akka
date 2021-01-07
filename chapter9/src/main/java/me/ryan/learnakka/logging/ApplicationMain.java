package me.ryan.learnakka.logging;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ApplicationMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MyActorSystem");

        ActorRef pingActor = system.actorOf(LoggingActor.props(), "pingActor");
        pingActor.tell("message1", null);
        pingActor.tell("message2", null);
        pingActor.tell("message3", null);
        system.terminate();
    }
}
