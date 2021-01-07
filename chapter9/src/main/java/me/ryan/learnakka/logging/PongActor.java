package me.ryan.learnakka.logging;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PongActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(PongActor.class);
    }

    public static class PongMessage {
        private final String text;

        public PongMessage(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LoggingActor.PingMessage.class, text -> {
                    log.info("In PongActor - received message: {}", text);
                getSender().tell(new PongMessage("pong"), getSelf());
                })
                .build();
    }
}
