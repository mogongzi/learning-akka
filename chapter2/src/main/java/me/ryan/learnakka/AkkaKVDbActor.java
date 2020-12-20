package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import me.ryan.learnakka.message.GetRequest;
import me.ryan.learnakka.message.KeyNotFoundException;
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
                    log.info("Receive Set request: {}", message);
                    map.put(message.getKey(), message.getValue());
                    sender().tell(new Status.Success(message.getKey()), self());
                })
                .match(GetRequest.class, message -> {
                    log.info("Receive Get request: {}", message);
                    Object value = map.get(message.getKey());
                    Object response = (value != null) ? value : new Status.Failure(new KeyNotFoundException(message.getKey()));
                    sender().tell(response, self());
                })
                .matchAny(o -> sender().tell(new Status.Failure(new ClassNotFoundException()), self()))
                .build();
    }
}
