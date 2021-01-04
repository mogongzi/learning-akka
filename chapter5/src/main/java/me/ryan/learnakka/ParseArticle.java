package me.ryan.learnakka;

public class ParseArticle {

    private final String htmlBody;

    public ParseArticle(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }
}
