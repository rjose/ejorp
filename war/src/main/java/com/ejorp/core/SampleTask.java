package com.ejorp.core;

import javax.persistence.*;

// TODO: Get rid of this class

/**
 * Created by IntelliJ IDEA.
 * User: rjose
 * Date: 11/13/11
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@NamedQuery(name = "findAllTasks", query = "SELECT t FROM SampleTask t")
public class SampleTask {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private Long assigneeId;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
