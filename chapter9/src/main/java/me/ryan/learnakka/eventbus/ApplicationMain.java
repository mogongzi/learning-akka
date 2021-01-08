package me.ryan.learnakka.eventbus;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ApplicationMain {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("MyActorSystem");
        ActorRef greetingActor = system.actorOf(Props.create(GreetingActor.class));

        JavaLookupClassifier jLookupBus = new JavaLookupClassifier();
        jLookupBus.subscribe(greetingActor, "java-greetings");
        jLookupBus.publish(new EventBusMessage("time", String.valueOf(System.currentTimeMillis())));
        jLookupBus.publish(new EventBusMessage("java-greetings", "java event bus greeting"));

        ScalaLookupClassifier sLookupBus = new ScalaLookupClassifier();
        sLookupBus.subscribe(greetingActor, "scala-greetings");
        sLookupBus.publish(new EventBusMessage("time", String.valueOf(System.currentTimeMillis())));
        sLookupBus.publish(new EventBusMessage("scala-greetings", "scala event bus greeting"));

        system.terminate();
    }

    static class GreetingActor extends AbstractActor {

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .matchAny(x -> System.out.println("guess we received a greeting! msg: " + x))
                    .build();
        }
    }
}
