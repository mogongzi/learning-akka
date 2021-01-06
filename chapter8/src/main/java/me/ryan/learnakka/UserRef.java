package me.ryan.learnakka;

import akka.actor.ActorRef;

public class UserRef {

    private final ActorRef actor;
    private final String username;

    public UserRef(ActorRef actor, String username) {
        this.actor = actor;
        this.username = username;
    }

    public ActorRef getActor() {
        return actor;
    }

    public String getUsername() {
        return username;
    }
}
