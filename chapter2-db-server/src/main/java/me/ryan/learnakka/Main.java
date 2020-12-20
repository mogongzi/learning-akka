package me.ryan.learnakka;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("akkademy");
        system.actorOf(Props.create(AkkaKVDbActor.class), "akkademy-db");
    }
}
