package me.ryan.learnakka.message;

import java.io.Serializable;

public class KeyExistsException extends Exception implements Serializable {
    private final String key;

    public KeyExistsException(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
