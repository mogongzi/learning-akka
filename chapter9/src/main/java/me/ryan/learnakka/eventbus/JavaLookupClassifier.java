package me.ryan.learnakka.eventbus;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

public class JavaLookupClassifier extends LookupEventBus<EventBusMessage, ActorRef, String> {

    @Override
    public int mapSize() {
        return 128;
    }

    @Override
    public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    @Override
    public String classify(EventBusMessage event) {
        return event.getTopic();
    }

    @Override
    public void publish(EventBusMessage event, ActorRef subscriber) {
        subscriber.tell(event.getMsg(), ActorRef.noSender());
    }
}
