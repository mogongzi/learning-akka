package me.ryan.learnakka.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class SetRequest implements Serializable, Request {
    private final String key;
    private final Object value;
    private final ActorRef sender;

    public SetRequest(String key, Object value) {
        this.key = key;
        this.value = value;
        this.sender = ActorRef.noSender();
    }

    public SetRequest(String key, Object value, ActorRef sender) {
        this.key = key;
        this.value = value;
        this.sender = sender;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public ActorRef getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "SetRequest{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", sender=" + sender +
                '}';
    }
}
