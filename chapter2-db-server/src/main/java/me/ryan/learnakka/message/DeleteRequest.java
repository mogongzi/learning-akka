package me.ryan.learnakka.message;

import java.io.Serializable;

public class DeleteRequest implements Serializable {
    private final String key;

    public DeleteRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
