package me.ryan.learnakka;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import me.ryan.learnakka.message.GetRequest;
import me.ryan.learnakka.message.SetRequest;
import scala.jdk.javaapi.FutureConverters;

import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

public class JClient {

    private final ActorSystem system = ActorSystem.create("LocalSystem");
    private final ActorSelection remoteDb;

    public JClient(String remoteAddress) {
        this.remoteDb = system.actorSelection("akka.tcp://akkademy@" + remoteAddress + "/user/akkademy-db");
    }

    public CompletionStage set(String key, Object value) {
        return FutureConverters.asJava(ask(remoteDb, new SetRequest(key, value), 2000));
    }

    public CompletionStage<Object> get(String key) {
        return FutureConverters.asJava(ask(remoteDb, new GetRequest(key), 2000));
    }
}
