package me.ryan.learnakka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatroomTest {

    static ActorSystem system = ActorSystem.apply();

    @Test
    public void testShouldAddUserToJoinedUsersWhenJoiningTest() {
        Props props = Props.create(Chatroom.class);

        TestActorRef<Chatroom> ref = TestActorRef.create(system, props, "testA");
        Chatroom chatroom = ref.underlyingActor();
        assertEquals(0, chatroom.joinedUsers.size());

        UserRef userRef = new UserRef(system.deadLetters(), "user");
        Messages.JoinChatroom request = new Messages.JoinChatroom(userRef);
        // ref.tell(request, system.deadLetters());
        chatroom.joinChatroom(request); // access Actor directly instead of sending message to the Actor
        assertEquals(userRef, chatroom.joinedUsers.get(0));
    }
}