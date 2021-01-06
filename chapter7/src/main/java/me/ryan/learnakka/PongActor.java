package me.ryan.learnakka;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PongActor extends UntypedAbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

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
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof PingActor.PingMessage) {
            Thread.sleep(1000);
            PingActor.PingMessage ping = (PingActor.PingMessage) message;
            log.info("In PongActor - received message:{}", ping.getText());
            getSender().tell(new PongMessage("pong"), getSelf());
        } else {
            unhandled(message);
        }
    }
}
