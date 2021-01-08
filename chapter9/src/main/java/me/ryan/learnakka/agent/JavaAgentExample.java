package me.ryan.learnakka.agent;

import akka.actor.ActorSystem;
import akka.agent.Agent;
import akka.dispatch.Mapper;

public class JavaAgentExample {

    public static void apply() throws InterruptedException {
        ActorSystem system = ActorSystem.create();
        Agent<Integer> account = Agent.create(25, system.dispatcher());

        final Integer ammountToWithdraw = 20;

        account.send(new Mapper<>() {
            @Override
            public Integer apply(Integer i) {
                if (ammountToWithdraw <= i) {
                    return i - ammountToWithdraw;
                } else {
                    return i;
                }
            }
        });

        Thread.sleep(1_000);
        System.out.println(account.get());

        account.send(new Mapper<>() {
            @Override
            public Integer apply(Integer i) {
                if (ammountToWithdraw <= i) {
                    return i - ammountToWithdraw;
                } else {
                    return i;
                }
            }
        });

        Thread.sleep(1_000);
        System.out.println(account.get());

        system.terminate();
    }
}
