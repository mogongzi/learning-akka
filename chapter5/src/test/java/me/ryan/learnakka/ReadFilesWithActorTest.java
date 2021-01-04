package me.ryan.learnakka;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import com.jasongoodwin.monads.Futures;
import org.junit.Test;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class ReadFilesWithActorTest {

    ActorSystem system = ActorSystem.create();

    @Test
    public void shouldReadFilesWithActors() throws ExecutionException, InterruptedException {
        ActorRef workerRouter = system.actorOf(Props.create(ArticleParseActor.class)
                .withDispatcher("my-dispatcher")
                .withRouter(new RoundRobinPool(8)));

        CompletableFuture future = new CompletableFuture();
        ActorRef cameoActor = system.actorOf(Props.create(TestCameoActor.class, future));

        IntStream.range(0, 2000).forEach(x -> {
            workerRouter.tell(new ParseArticle(TestHelper.file), cameoActor);
        });

        long start = System.currentTimeMillis();
        future.get();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("ReadFilesWithFuturesTest took: " + elapsedTime);

    }
}
