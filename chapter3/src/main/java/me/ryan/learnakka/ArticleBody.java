package me.ryan.learnakka;

public class ArticleBody {
    private final String uri;
    private final String body;

    public ArticleBody(String uri, String body) {
        this.uri = uri;
        this.body = body;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }
}
