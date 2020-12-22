package me.ryan.learnakka;

public class ParseHtmlArticle {
    private final String uri;
    private final String html;

    public ParseHtmlArticle(String uri, String html) {
        this.uri = uri;
        this.html = html;
    }

    public String getUri() {
        return uri;
    }

    public String getHtml() {
        return html;
    }
}
