package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;

public class PingActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Ping", s -> {
                    sender().tell("Pong", ActorRef.noSender());
                })
                .matchAny(x -> sender().tell(new Status.Failure(new Exception("Unknown message")), self()))
                .build();
    }
}
