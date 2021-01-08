package me.ryan.learnakka.eventbus;

public class EventBusMessage {

    private final String topic;
    private final String msg;

    public EventBusMessage(String topic, String msg) {
        this.topic = topic;
        this.msg = msg;
    }

    public String getTopic() {
        return topic;
    }

    public String getMsg() {
        return msg;
    }
}
