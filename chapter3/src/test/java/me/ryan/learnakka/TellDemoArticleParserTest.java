package me.ryan.learnakka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestProbe;
import akka.util.Timeout;
import me.ryan.learnakka.message.GetRequest;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.junit.Assert.*;

public class TellDemoArticleParserTest {

    ActorSystem system = ActorSystem.create("testSystem");
    Timeout timeout = Timeout.durationToTimeout(Duration.create(10000, TimeUnit.MILLISECONDS));

    TestProbe cacheProbe = new TestProbe(system);
    TestProbe httpClientProbe = new TestProbe(system);
    ActorRef articleParserActor = system.actorOf(Props.create(ParsingActor.class));

    ActorRef tellDemoActor = system.actorOf(Props.create(TellDemoArticleParser.class, cacheProbe.ref().path().toString(),
            httpClientProbe.ref().path().toString(), articleParserActor.path().toString(), timeout));

    @Test
    public void itShouldParseArticleTest() throws Exception {
        Future f = ask(tellDemoActor, new ParseArticle("https://www.w3.org/2003/08/system-status"), timeout);
        cacheProbe.expectMsgClass(GetRequest.class);
        cacheProbe.reply(new Status.Failure(new Exception("no cache")));

        httpClientProbe.expectMsgClass(String.class);
        httpClientProbe.reply(new HttpResponse(Articles.article1));

        String result = (String) Await.result(f, timeout.duration());
        assertTrue(result.contains("We are facing hardware problems and some services"));
        assertFalse(result.contains("<body>"));
    }
}