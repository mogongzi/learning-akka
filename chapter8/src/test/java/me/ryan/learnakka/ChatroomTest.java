package me.ryan.learnakka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChatroomTest {

    static ActorSystem system = ActorSystem.apply();

    @Test
    public void testShouldAddUserToJoinedUsersWhenJoiningTest() {
        // Given a Chatroom has no users
        Props props = Props.create(Chatroom.class);
        TestActorRef<Chatroom> ref = TestActorRef.create(system, props, "testA");
        Chatroom chatroom = ref.underlyingActor();
        assertEquals(0, chatroom.joinedUsers.size());

        // When it receives a request from a user to join the chatroom
        UserRef userRef = new UserRef(system.deadLetters(), "user");
        Messages.JoinChatroom request = new Messages.JoinChatroom(userRef);
        // ref.tell(request, system.deadLetters());
        chatroom.joinChatroom(request); // access Actor directly instead of sending message to the Actor

        // It should add the UserRef to its list of joined users
        assertEquals(userRef, chatroom.joinedUsers.get(0));
    }

    @Test
    public void testShouldSendHistoryWhenUserJoin() {
        new TestKit(system) {{
            // Given
            Props props = Props.create(Chatroom.class);
            TestActorRef<Chatroom> ref = TestActorRef.create(system, props);
            Chatroom chatroom = ref.underlyingActor();
            Messages.PostToChatroom msg = new Messages.PostToChatroom("test", "user");
            chatroom.chatHistory.add(msg);

            // When
            UserRef userRef = new UserRef(system.deadLetters(), "user");
            Messages.JoinChatroom request = new Messages.JoinChatroom(userRef);
            ref.tell(request, getRef());

            // Then
            List<Messages.PostToChatroom> expected = new ArrayList<>();
            expected.add(msg);
            expectMsgEquals(Duration.ofSeconds(1), expected);
        }};
    }

    @Test
    public void testShouldSendUpdateWhenUserPosts() {
        // Given
        Props props = Props.create(Chatroom.class);
        TestActorRef<Chatroom> ref = TestActorRef.create(system, props);
        Chatroom chatroom = ref.underlyingActor();
        final TestProbe probe = new TestProbe(system);
        UserRef userRef = new UserRef(probe.ref(), "user");
        chatroom.joinChatroom(new Messages.JoinChatroom(userRef));

        // When
        Messages.PostToChatroom msg = new Messages.PostToChatroom("test", "user");
        ref.tell(msg, probe.ref());

        // Then
        probe.expectMsg(msg);
    }
}