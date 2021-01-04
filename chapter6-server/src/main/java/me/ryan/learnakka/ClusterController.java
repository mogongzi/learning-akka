package me.ryan.learnakka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;

import static akka.cluster.ClusterEvent.*;

public class ClusterController extends AbstractActor {

    protected final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    Cluster cluster = Cluster.get(getContext().system());

    List<ActorRef> workers = new ArrayList<>();

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), (SubscriptionInitialStateMode) ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MemberEvent.class, message -> {
                    if (message.getClass() == MemberUp.class) {
                        System.out.println("member up: " + message.member().address());
                    }
                    log.info("MemberEvent: {}", message);
                })
                .match(UnreachableMember.class, message -> {
                    log.info("UnreachableMember: {}", message);
                })
                .build();
    }
}
