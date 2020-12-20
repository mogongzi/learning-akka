package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MsgActor extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(this.context().system(), this);
    protected String savedMsg = "";

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    log.info("received message is: {}", message);
                    this.savedMsg = message;
                })
                .matchAny(o -> log.info("received unknown message: {}", o))
                .build();
    }
}
