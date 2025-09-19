package org.grow.db.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.grow.db.DBConnection;
import org.grow.db.dao.AppointmentDAO;
import org.grow.db.pojo.AppointmentDB;

import java.sql.Timestamp;
import java.util.List;

@Log4j2
public class AppointmentDBService {

    private AppointmentDBService() {
        // NONE
    }


    public static void insertAppointment(AppointmentDB appointment) {
        log.debug("(DB) Insert appointment: '{}'", appointment);

        DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .insertAppointment(appointment);
    }

    public static List<AppointmentDB> getAppointmentsBy(@NotNull String roomName) {
        log.debug("(DB) Get appointments with room: '{}'", roomName);

        return DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .getAppointmentsBy(roomName);
    }

    public static List<AppointmentDB> getAppointmentsConfirmedForTodayOrTomorrowFor(long chatId) {
        log.debug("(DB) Get appointments for tomorrow by chatId: '{}'", chatId);

        return DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .getAppointmentsConfirmedForTodayOrTomorrowFor(chatId);
    }

    public static boolean isAppointmentExist(@NotNull String roomName, long chatId, Timestamp eventStartDate) {
        log.debug("(DB) Check is appointment exist (room: {}, chatId: '{}', start date: {}).", roomName, chatId, eventStartDate);

        return DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .isAppointmentExist(roomName, chatId, eventStartDate);
    }

    public static void updateAppointmentStatusWith(boolean newStatus, long chatId, Timestamp eventStartDate) {
        log.debug("(DB) Update appointment's with a new status: '{}' for charId: {}, event start date: {}", newStatus, chatId, eventStartDate);

        DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .updateAppointmentStatusWith(newStatus, chatId, eventStartDate);
    }

    public static void updateAppointmentImageIdFor(@NotNull String imageId, int appId) {
        log.debug("(DB) Update appointment: {}, with imageID: '{}'", appId, imageId);

        DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .updateAppointmentImageIdFor(imageId, appId);
    }

    public static void deleteAppointmentWithRoomNameContains(@NotNull String value) {
        log.debug("(DB) Delete appointments where room contains: '{}'", value);

        DBConnection.createConnection()
                .onDemand(AppointmentDAO.class)
                .deleteAppointmentWithRoomNameContains(value);
    }
}
