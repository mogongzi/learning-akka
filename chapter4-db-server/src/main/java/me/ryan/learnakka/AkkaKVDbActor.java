package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import me.ryan.learnakka.message.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AkkaKVDbActor extends AbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(this.context().system(), this);
    protected final Map<String, Object> map = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Ping.class, message -> {
                    sender().tell("Connected", self());
                })
                .match(Connected.class, message -> {
                    log.info("Received Connected request: {}", message);
                    sender().tell(new Connected(), self());
                })
                .match(List.class, message -> {
                    message.forEach(x -> {
                        if (x instanceof SetRequest) {
                            SetRequest setRequest = (SetRequest) x;
                            handleSetRequest(setRequest);
                        }
                        if (x instanceof GetRequest) {
                            GetRequest getRequest = (GetRequest) x;
                            handleGetRequest(getRequest);
                        }
                    });
                })
                .match(SetRequest.class, this::handleSetRequest)
                .match(GetRequest.class, this::handleGetRequest)
                .matchAny(o -> {
                    log.info("Unknown message: {}", o);
                    sender().tell(new Status.Failure(new ClassNotFoundException()), self());
                })
                .build();
    }

    private void handleSetRequest(SetRequest message) {
        log.info("Receive Set request: {}", message);
        map.put(message.getKey(), message.getValue());
        message.getSender().tell(new Status.Success(message.getKey()), self());
    }

    private void handleGetRequest(GetRequest message) {
        log.info("Receive Get request: {}", message);
        Object value = map.get(message.getKey());
        Object response = (value != null) ? value : new Status.Failure(new KeyNotFoundException(message.getKey()));
        sender().tell(response, self());
    }
}
