package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import me.ryan.learnakka.message.SetRequest;

import java.util.HashMap;
import java.util.Map;

public class AkkaKVDbActor extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(this.context().system(), this);
    protected final Map<String, Object> map = new HashMap<>();


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SetRequest.class, message -> {
                    log.info("received Set Request: {}", message);
                    map.put(message.getKey(), message.getValue());
                })
                .matchAny(o -> log.info("received unknown message: {}", o))
                .build();
    }
}
