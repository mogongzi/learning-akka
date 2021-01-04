package me.ryan.learnakka;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorSelection;
import me.ryan.learnakka.message.Connected;
import me.ryan.learnakka.message.Ping;
import me.ryan.learnakka.message.Request;

public class IdentifyRemoteHotswapClientActor extends AbstractActorWithStash {

    private ActorSelection remoteDb;

    private final Receive disconnected;
    private final Receive online;

    public IdentifyRemoteHotswapClientActor(String remoteAddress) {
        this.remoteDb = getContext().actorSelection(remoteAddress);

        this.online = receiveBuilder()
                .match(Request.class, x -> remoteDb.forward(x, getContext()))
                .match(Ping.class, x -> remoteDb.forward(x, getContext()))
                .build();

        this.disconnected = receiveBuilder()
                .match(Request.class, x -> {
                    remoteDb.tell(new Connected(), self());
                    stash();
                })
                .match(Ping.class, x -> {
                    remoteDb.tell(new Connected(), self());
                    stash();
                })
                .match(Connected.class, x -> {
                    getContext().become(online);
                    unstash();
                })
                .build();
    }

    @Override
    public Receive createReceive() {
        return disconnected;
    }
}
