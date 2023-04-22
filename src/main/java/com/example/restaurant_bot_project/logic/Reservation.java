package com.example.restaurant_bot_project.logic;

public class Reservation {
    private Integer id;
    private String email;
    private String phone;
    private String telegram;
    private String reservation_date;
    private String reservation_time;
    private Integer table_id;

    public Reservation(Integer id, String email, String phone, String telegram, String reservation_date, String reservation_time, Integer table_id) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.telegram = telegram;
        this.reservation_date = reservation_date;
        this.reservation_time = reservation_time;
        this.table_id = table_id;
    }

    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }

    public Integer getTable_id() {
        return table_id;
    }

    public void setTable_id(Integer table_id) {
        this.table_id = table_id;
    }
}
