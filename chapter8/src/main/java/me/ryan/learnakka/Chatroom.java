package me.ryan.learnakka;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.List;

public class Chatroom extends AbstractActor {

    List<Messages.PostToChatroom> chatHistory = new ArrayList<>();
    List<UserRef> joinedUsers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.JoinChatroom.class, this::joinChatroom)
                .matchAny(o -> System.out.println("received unknown message"))
                .build();
    }

    public void joinChatroom(Messages.JoinChatroom message) {
        joinedUsers.add(message.userRef);
    }
}
