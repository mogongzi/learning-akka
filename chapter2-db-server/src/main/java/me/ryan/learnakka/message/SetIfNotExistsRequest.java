package me.ryan.learnakka.message;

import java.io.Serializable;

public class SetIfNotExistsRequest implements Serializable {
    private final String key;
    private final Object value;

    public SetIfNotExistsRequest(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
