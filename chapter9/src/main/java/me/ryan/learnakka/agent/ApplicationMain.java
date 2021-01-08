package me.ryan.learnakka.agent;

public class ApplicationMain {

    public static void main(String[] args) throws InterruptedException {
        JavaAgentExample.apply();
        ScalaAgentExample.apply();
        ScalaAgentExample.multipleTransaction();
    }
}
