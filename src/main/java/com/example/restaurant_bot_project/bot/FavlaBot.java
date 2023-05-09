package com.example.restaurant_bot_project.bot;
import com.example.restaurant_bot_project.logic.Reservation;
import com.example.restaurant_bot_project.repository.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FavlaBot extends TelegramLongPollingBot{
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    @Autowired
    private Dao dao;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                long chatId = message.getChatId();

                        if (text.matches("^[\\/][s][t][a][r][t]")) {
                            startMessage(chatId);
                        } else if (text.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                            mailMessage(chatId, text);
                        } else if (text.matches("^[(][0-9]{3}[)][-][0-9]{3}[-][0-9]{2}[-][0-9]{2}$")) {
                            phoneMessage(chatId, text);
                        }else {
                            System.out.println(text);
                        }

            }
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callback = update.getCallbackQuery();
            String text = callback.getData();
            System.out.println("my CALLBACK: " + text);
            long chatId = callback.getMessage().getChatId();
            if (text.matches("^[\\/][d][a][y][^\\>]*$")) {
                System.out.println("we in DAY block");
                String data = text.replace("/day ", "");
                choseDay(chatId, data);
            } else if (text.matches("^[\\/][t][i][m][e][^\\>]*$")) {
                String data = text.replace("/time ", "");
                choseTime(chatId, data);
            } else if (text.matches("^[\\/][t][a][b][l][e][^\\>]*$")) {
                String data = text.replace("/table ", "");
                choseTable(chatId, data);
            } else if (text.matches("^[\\/][c][o][n][f][i][r][m][^\\>]*$")) {
                String data = text.replace("/confirm ", "");
                confirm(chatId, data);
            }else {
                System.out.println(text);
            }
        }
    }

    private void sendMessage(long chatId, String text){
        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(String.valueOf(chatId));
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendButtonsMessage(long chatId, String text, List<String> buttons, String action){
        List<List<InlineKeyboardButton>> bts = new ArrayList<>();

        for(String button : buttons){
            String callback = "" + action + " " + button;
            bts.add(Arrays.asList(InlineKeyboardButton.builder().text(button).callbackData(callback).build()));
        }
        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(String.valueOf(chatId));
        sm.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(bts).build());
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTablesPhoto(long chatId){
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(chatId));
            File plan = ResourceUtils.getFile("/root/Plan.jpg");
            sendPhoto.setPhoto(new InputFile(plan, "name"));
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void startMessage(long chatId) {
        String text = "You are welcomed by cafe Favla. In this bot you can book a table, to do this, first enter the mail to which the reservation will be created";
        sendMessage(chatId, text);
        Reservation reservation = new Reservation();
        reservation.setTelegram(String.valueOf(chatId));
        dao.start(reservation);
    }

    private void mailMessage(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setEmail(text);
        reservation.setTelegram(String.valueOf(chatId));
        dao.setEmail(reservation);

        String message = "Great, now enter your phone number so we can contact you if needed.  Enter it in the format (XXX)-XXX-XX-XX";
        sendMessage(chatId, message);
    }

    private void phoneMessage(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setPhone(text);
        reservation.setTelegram(String.valueOf(chatId));
        dao.setPhone(reservation);
        String message = "Ok, now select the day you want to make a reservation";
        //тут пользователю должно отправляться сообщение с кнопками доступной даты
        LocalDateTime now = LocalDateTime.now();
        List<String> days = new ArrayList<>();
        System.out.println(now);
        YearMonth yearMonthObject = YearMonth.of(now.getYear(), now.getMonthValue());
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int daysTillEndOfMonth = daysInMonth - now.getDayOfMonth();
        if(daysTillEndOfMonth >=7){
            for(int i = now.getDayOfMonth(); i <= now.getDayOfMonth() + 6; i++){
                String day = toStringDay(i, now.getMonthValue(), now.getYear());
                days.add(day);
            }
        }else {
            for(int i = now.getDayOfMonth(); i <= now.getDayOfMonth() + daysTillEndOfMonth; i++){
                String day = toStringDay(i, now.getMonthValue(), now.getYear());
                days.add(day);
            }
            if(now.getMonthValue() != 12){
                int month = now.getMonthValue() + 1;
                for(int i = 1; i <= 7 - daysTillEndOfMonth; i++){
                    String day = toStringDay(i, month, now.getYear());
                    days.add(day);
                }
            }else {
                int year = now.getYear() + 1;
                for(int i = 1; i <= 7 - daysTillEndOfMonth; i++){
                    String day = toStringDay(i, 1, year);
                    days.add(day);
                }
            }

        }
        String action = "/day";
        sendButtonsMessage(chatId, message, days, action);
    }

    //вставку будем делать в последнюю запись бд с чатайди

    //если надо будет менятоь резервацию то будут все другрие методы

    private void choseDay(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setReservation_date(text);
        reservation.setTelegram(String.valueOf(chatId));
        dao.setDate(reservation);

        String message = "Good, just a few steps left. Choose time";
        List<String> times = new ArrayList<>();
        times.add("11:00-13:00");
        times.add("13:00-15:00");
        times.add("15:00-17:00");
        times.add("17:00-19:00");
        times.add("19:00-21:00");
        times.add("21:00-23:00");
        String action = "/time";
        sendButtonsMessage(chatId, message, times, action);
    }

    private String toStringDay(int day, int month, int year){
        String d;
        String m;
        if(day > 9){
            d = "" + day;
        }else {
            d ="0" + day;
        }
        if(month > 9){
            m = "" + month;
        }else {
            m ="0" + month;
        }

        String thisDay = "" + d + "." + m + "." + year;
        return thisDay;
    }

    private void choseTime(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setReservation_time(text);
        reservation.setTelegram(String.valueOf(chatId));
        //dao.setTime(reservation);
        //!!!!!!!!!
        String message = "Choose table";
        sendTablesPhoto(chatId);
        List<String> emptyTables = dao.findTables(reservation);
        //в бд найти доступные столы по дате и времени

        List<String> tables = new ArrayList<>();
        String action = "/table";
        sendButtonsMessage(chatId, message, tables, action);
    }

    private void choseTable(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setTable_id(Integer.valueOf(text));
        reservation.setTelegram(String.valueOf(chatId));
        dao.setTable(reservation);

        Reservation res = dao.getNotReadyReservation(reservation);
        String message = "Ok, now lets check what you choose:  " +
                "Email: " + res.getEmail() +
                "Phone: " + res.getPhone() +
                "Date: " + res.getReservation_date() +
                "Time: " + res.getReservation_time()+
                "Table: " + res.getTable_id();
        String action = "/confirm";
        List<String> buttons = new ArrayList<>();
        buttons.add("Confirm");
        sendButtonsMessage(chatId, message, buttons, action);
        // готово просмотрине данные регистрации
        //Пишем что регистрация готова, ее номер такой то и инструкции если мы хотим изменить резервацию
    }

    private void confirm(long chatId, String text) {
        Reservation reservation = new Reservation();
        reservation.setTelegram(String.valueOf(chatId));
        Integer id = dao.confirmReservation(reservation);
        String message = "Great, your reservation №" + id + " is confirmed!";
        sendMessage(chatId, message);
    }

}