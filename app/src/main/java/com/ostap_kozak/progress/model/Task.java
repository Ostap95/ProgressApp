package com.ostap_kozak.progress.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ostapkozak on 08/09/2016.
 */
public class Task extends RealmObject{

    @PrimaryKey
    private String taskName;
    private long _id;

    private boolean active = false;

    public Task() { this.taskName = "Task";}

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setId(long id) { _id = id; }

    public long getId() { return _id; }

}
