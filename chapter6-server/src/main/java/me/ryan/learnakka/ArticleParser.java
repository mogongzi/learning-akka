package me.ryan.learnakka;

import com.jasongoodwin.monads.Try;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ArticleParser {

    public static Try<String> apply(String html) {
        return Try.ofFailable(() -> ArticleExtractor.INSTANCE.getText(html));
    }
}
