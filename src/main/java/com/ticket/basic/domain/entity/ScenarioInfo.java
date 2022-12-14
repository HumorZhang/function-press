package com.ticket.basic.domain.entity;

import java.util.Date;

public class ScenarioInfo {
    private Integer id;
    private String scenarioName;
    private String scenarioDescription;
    private Date createTime;
    private Integer numThreads;
    private Integer rampUp;
    private Integer duration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getScenarioDescription() {
        return scenarioDescription;
    }

    public void setScenarioDescription(String scenarioDescription) {
        this.scenarioDescription = scenarioDescription;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(Integer numThreads) {
        this.numThreads = numThreads;
    }

    public Integer getRampUp() {
        return rampUp;
    }

    public void setRampUp(Integer rampUp) {
        this.rampUp = rampUp;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ScenarioInfo{" +
                "id=" + id +
                ", scenarioName='" + scenarioName + '\'' +
                ", scenarioDescription='" + scenarioDescription + '\'' +
                ", createTime=" + createTime +
                ", numThreads=" + numThreads +
                ", rampUp=" + rampUp +
                ", duration=" + duration +
                '}';
    }
}
