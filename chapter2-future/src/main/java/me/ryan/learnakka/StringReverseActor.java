package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import org.apache.commons.lang3.StringUtils;

public class StringReverseActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    sender().tell(StringUtils.reverse(message), ActorRef.noSender());
                })
                .matchAny(x -> sender().tell(new Status.Failure(new Exception("unknown message")), self()))
                .build();
    }
}
