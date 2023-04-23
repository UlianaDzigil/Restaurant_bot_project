package com.example.restaurant_bot_project.repository;

import com.example.restaurant_bot_project.logic.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class Dao {

    @Autowired
    private GeneratorId reservationId;

    public Dao() {
    }

    /*public List<Dish> getDishes(){
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "SELECT * FROM dishes";
        final String user = "postgres";
        final String password = "postgres";
        ArrayList<Dish> dishes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer cost = rs.getInt("cost_uah");
                Integer type = rs.getInt("type_id");
                String description = rs.getString("description");
                String image = rs.getString("image");
                Dish dish = new Dish(id, name, cost, type, description, image);

                dishes.add(dish);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dishes;
    }*/

    /*public List<Reservation> setEmail(Reservation reservation){
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "INSERT INTO reservations(id, type)\n" +
                "VALUES (1, 'Закуски');";
        final String user = "postgres";
        final String password = "postgres";
        *//*ArrayList<Dish> dishes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer cost = rs.getInt("cost_uah");
                Integer type = rs.getInt("type_id");
                String description = rs.getString("description");
                String image = rs.getString("image");
                Dish dish = new Dish(id, name, cost, type, description, image);

                dishes.add(dish);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
*//*
        List<Reservation> res = new ArrayList<>();
        return res;
    }*/
    public void setEmail(Reservation reservation){
        String email = reservation.getEmail();
        String telegram = reservation.getTelegram();
        Integer id = reservationId.getId();
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "INSERT INTO reservations(id, email, telegram) VALUES (?, ?, ?);";
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, email);
            ps.setString(3, telegram);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void setPhone(Reservation reservation){
        String phone = reservation.getPhone();
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        final String url = "jdbc:postgresql://db:5432/favla";
        /*final String sql = "INSERT INTO reservations(id, phone) VALUES (?, ?);";*/
        final String sql = "UPDATE reservations SET phone = ? WHERE id = ?;";
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setString(1, phone);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setDate(Reservation reservation){
        String date = reservation.getReservation_date();
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        final String url = "jdbc:postgresql://db:5432/favla";
        /*final String sql = "INSERT INTO reservations(id, reservation_date) VALUES (?, ?);";*/
        final String sql = "UPDATE reservations SET reservation_date = ? WHERE id = ?;";
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setString(1, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTime(Reservation reservation){
        String time = reservation.getReservation_time();
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "UPDATE reservations SET reservation_time = ? WHERE id = ?;";
        /*final String sql = "INSERT INTO reservations(id, reservation_time) VALUES (?, ?);";*/
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setString(1, time);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTable(Reservation reservation){
        Integer table = reservation.getTable_id();
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "UPDATE reservations SET table_id = ? WHERE id = ?;";
        /*final String sql = "INSERT INTO reservations(id, table_id) VALUES (?, ?);";*/
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setInt(1, table);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setReadyReservationById(id);
    }

    public void setReadyReservationById(Integer reservationId){
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "UPDATE reservations SET isready = TRUE WHERE id = ?;";
        /*final String sql = "INSERT INTO reservations(id, table_id) VALUES (?, ?);";*/
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Reservation getReservation(Reservation reservation){
        List<Integer> idList = getReadyReservationIdByTelegram(reservation);
        Reservation reservation1 = new Reservation();
        Integer id = idList.get(idList.size() - 1);
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "SELECT * FROM reservations WHERE id = ?";
        final String user = "postgres";
        final String password = "postgres";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservation1.setId(id);
                reservation1.setEmail(rs.getString("email"));
                reservation1.setPhone(rs.getString("phone"));
                reservation1.setReservation_date(rs.getString("reservation_date"));
                reservation1.setReservation_time(rs.getString("reservation_time"));
                reservation1.setTable_id(rs.getInt("table_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservation1;
    }

    public List<Integer> getNotReadyReservationIdByTelegram(Reservation reservation){
        List<Integer> idList = new ArrayList<>();
        String telegram = reservation.getTelegram();
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "SELECT * FROM reservations WHERE telegram = ? AND isready = FALSE";
        final String user = "postgres";
        final String password = "postgres";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telegram);
            ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    idList.add(id);
                }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idList;
    }

    public List<Integer> getReadyReservationIdByTelegram(Reservation reservation){
        List<Integer> idList = new ArrayList<>();
        String telegram = reservation.getTelegram();
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "SELECT * FROM reservations WHERE telegram = ? AND isready = TRUE";
        final String user = "postgres";
        final String password = "postgres";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telegram);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                idList.add(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idList;
    }
}

