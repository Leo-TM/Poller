package com.softians.poller.model;

/**
 * Created by HP on 05-09-2017.
 */

public class TopicList {
    private int id;
    String pTopicName;
    public String pEndTimer;

    public TopicList(int id, String pEndTimer, String pTopicName) {
        this.id = id;
        this.pEndTimer = pEndTimer;
        this.pTopicName = pTopicName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpEndTimer() {
        return pEndTimer;
    }

    public void setpEndTimer(String pEndTimer) {
        this.pEndTimer = pEndTimer;
    }

    public String getpTopicName() {
        return pTopicName;
    }

    public void setpTopicName(String pTopicName) {
        this.pTopicName = pTopicName;
    }
}
