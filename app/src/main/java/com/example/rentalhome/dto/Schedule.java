package com.example.rentalhome.dto;

public class Schedule {
    private String scheduleId;
    private String roomId;
    private String address;
    private String userId;
    private String dayOfWeek;
    private String from;
    private String to;
    private String status;

    public Schedule() {}

    public Schedule(String roomId, String address, String dayOfWeek, String from, String to) {
        this.roomId = roomId;
        this.address = address;
        this.dayOfWeek = dayOfWeek;
        this.from = from;
        this.to = to;
        this.status = "empty";
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
