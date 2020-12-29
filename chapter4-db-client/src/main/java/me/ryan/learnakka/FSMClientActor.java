package me.ryan.learnakka;

import akka.actor.AbstractFSM;
import akka.actor.ActorSelection;
import me.ryan.learnakka.message.Connected;
import me.ryan.learnakka.message.Request;

import static me.ryan.learnakka.State.*;

public class FSMClientActor extends AbstractFSM<State, EventQueue> {

    private ActorSelection remoteDb;

    public FSMClientActor(String remoteAddress) {
        this.remoteDb = context().actorSelection(remoteAddress);
    }

    {
        startWith(DISCONNECTED, new EventQueue());

        when(DISCONNECTED, matchEvent(FlushMessage.class, (msg, container) -> stay())
                .event(Request.class, (msg, container) -> {
                    remoteDb.tell(new Connected(), self());
                    container.add(msg);
                    return stay();
                })
                .event(Connected.class, (msg, container) -> {
                    if (container.size() == 0) {
                        return goTo(CONNECTED);
                    } else {
                        return goTo(CONNECTED_AND_PENDING);
                    }
                })
        );

        when(CONNECTED, matchEvent(FlushMessage.class, (msg, container) -> stay())
                .event(Request.class, (msg, container) -> {
                    container.add(msg);
                    return goTo(CONNECTED_AND_PENDING);
                })
        );

        when(CONNECTED_AND_PENDING, matchEvent(FlushMessage.class, (msg, container) -> {
                    remoteDb.tell(container, self());
                    container = new EventQueue();
                    return goTo(CONNECTED);
                }).event(Request.class, (msg, container) -> {
                    container.add(msg);
                    return goTo(CONNECTED_AND_PENDING);
                })
        );

        initialize();
    }
}
