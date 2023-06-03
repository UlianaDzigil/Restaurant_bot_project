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

    public void start(Reservation reservation){
        String telegram = reservation.getTelegram();
        //Integer id = reservationId.getId();
        final String url = "jdbc:postgresql://db:5432/favla";
        //final String sql = "INSERT INTO reservations(id, telegram, is_ready) VALUES (?, ?, FALSE);";
        final String sql = "INSERT INTO reservations( telegram, is_ready) VALUES ( ?, FALSE);";
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //ps.setInt(1, id);
            //ps.setString(2, telegram);
            ps.setString(1, telegram);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void setEmail(Reservation reservation){
        String email = reservation.getEmail();
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "UPDATE reservations SET email = ? WHERE id = ?;";
        final String user = "postgres";
        final String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(2, id);
            ps.setString(1, email);
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
        //setReadyReservationById(id);
    }


    public ArrayList<Integer> findTables(Reservation reservation){
        //find date and time
        Reservation reservation1 = getNotReadyReservation(reservation);
        String reservation_date = reservation1.getReservation_date();
        String reservation_time = reservation1.getReservation_time();

        //select free tables and add to array
        ArrayList<Integer> tables = findFreeTables(reservation_date, reservation_time);

        return tables;
    }

    public ArrayList<Integer> findFreeTables(String reservation_date, String reservation_time){
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "SELECT * FROM (SELECT id, table_id FROM reservations WHERE reservation_date=? AND reservation_time=?) r " +
                "RIGHT JOIN tables t ON r.table_id=t.id WHERE r.table_id is null;";
        final String user = "postgres";
        final String password = "postgres";
        ArrayList<Integer> tables = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reservation_date);
            ps.setString(2, reservation_time);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tables.add(rs.getInt("table_number"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    public void setReadyReservationById(Integer reservationId){
        final String url = "jdbc:postgresql://db:5432/favla";
        final String sql = "UPDATE reservations SET is_ready = TRUE WHERE id = ?;";
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

    public Integer confirmReservation(Reservation reservation){
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
        Integer id = idList.get(0);
        setReadyReservationById(id);
        return id;
    }

    public Reservation getNotReadyReservation(Reservation reservation){
        List<Integer> idList = getNotReadyReservationIdByTelegram(reservation);
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
        final String sql = "SELECT * FROM reservations WHERE telegram = ? AND is_ready = FALSE";
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
        final String sql = "SELECT * FROM reservations WHERE telegram = ? AND is_ready = TRUE";
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

