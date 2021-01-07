package me.ryan.learnakka.logging;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LoggingActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(LoggingActor.class);
    }

    public static class Initialize { }

    public static class PingMessage {
        private final String text;

        public PingMessage(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    private int counter = 0;
    private ActorRef pongActor = getContext().actorOf(PongActor.props(), "pongActor");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(x -> log.info("LoggingActor received event: {}", x.toString()))
                .build();
    }
}
