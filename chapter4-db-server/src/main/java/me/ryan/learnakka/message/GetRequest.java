package me.ryan.learnakka.message;

import akka.actor.ActorRef;

import java.io.Serializable;

public class GetRequest implements Serializable, Request {
    private final String key;
    private final ActorRef sender;

    public GetRequest(String key) {
        this.key = key;
        this.sender = ActorRef.noSender();
    }

    public GetRequest(String key, ActorRef sender) {
        this.key = key;
        this.sender = sender;
    }

    public String getKey() {
        return key;
    }

    public ActorRef getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "GetRequest{" +
                "key='" + key + '\'' +
                ", sender=" + sender +
                '}';
    }
}
