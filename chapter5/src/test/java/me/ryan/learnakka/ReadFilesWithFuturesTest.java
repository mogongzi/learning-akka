package me.ryan.learnakka;

import com.jasongoodwin.monads.Futures;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReadFilesWithFuturesTest {

    @Test
    public void  shouldReadFilesWithFutures() throws ExecutionException, InterruptedException {
        List<Integer> list = IntStream.range(0, 2000).boxed().collect(Collectors.toList());
        List futures = (List) list
                .stream()
                .map(x -> CompletableFuture.supplyAsync(() -> ArticleParser.apply(TestHelper.file)))
                .collect(Collectors.toList());

        long start = System.currentTimeMillis();
        Futures.sequence(futures).get();
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("ReadFilesWithFuturesTest took: " + elapsedTime);
    }
}
