package org.chudian.mj.bean;

public class Message {
    private Integer id;

    private Integer type;

    private String detail;

    private Integer ifFeedback;

    private String feedbackPlace;

    private String relatedService;

    private Integer status;

    private String keepword1;

    private String keepword2;

    private String keepword3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getIfFeedback() {
        return ifFeedback;
    }

    public void setIfFeedback(Integer ifFeedback) {
        this.ifFeedback = ifFeedback;
    }

    public String getFeedbackPlace() {
        return feedbackPlace;
    }

    public void setFeedbackPlace(String feedbackPlace) {
        this.feedbackPlace = feedbackPlace == null ? null : feedbackPlace.trim();
    }

    public String getRelatedService() {
        return relatedService;
    }

    public void setRelatedService(String relatedService) {
        this.relatedService = relatedService == null ? null : relatedService.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeepword1() {
        return keepword1;
    }

    public void setKeepword1(String keepword1) {
        this.keepword1 = keepword1 == null ? null : keepword1.trim();
    }

    public String getKeepword2() {
        return keepword2;
    }

    public void setKeepword2(String keepword2) {
        this.keepword2 = keepword2 == null ? null : keepword2.trim();
    }

    public String getKeepword3() {
        return keepword3;
    }

    public void setKeepword3(String keepword3) {
        this.keepword3 = keepword3 == null ? null : keepword3.trim();
    }
}