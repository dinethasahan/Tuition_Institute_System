package com.tuition.institute.notification;

public class NotificationDTO {
    private String id;
    private String type;
    private String title;
    private String body;
    private String channel;
    private String status;
    private String timestamp;

    public NotificationDTO() {}

    public NotificationDTO(String id, String type, String title, String body, String channel, String status, String timestamp) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.channel = channel;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
