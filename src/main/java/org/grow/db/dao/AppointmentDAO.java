package org.grow.db.dao;


import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jetbrains.annotations.NotNull;
import org.grow.db.pojo.AppointmentDB;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentDAO {

    @SqlUpdate("insert into appointments (room, " +
                "event_start, " +
                "chat_id, " +
                "image_id, " +
                "therapist_id) VALUES (:room, :eventStart, :chatId, :imageId, :therapistId)")
    @GetGeneratedKeys
    @RegisterBeanMapper(AppointmentDB.class)
    int insertAppointment(@BindBean AppointmentDB appointment);

    @SqlQuery("select * from appointments where room=:roomName;")
    @RegisterConstructorMapper(AppointmentDB.class)
    List<AppointmentDB> getAppointmentsBy(@NotNull String roomName);

    @SqlQuery("select * from appointments where chat_id=:chatId and status = 1 and (DATE(event_start) = CURRENT_DATE OR DATE(event_start) = CURRENT_DATE + INTERVAL 1 DAY);")
    @RegisterConstructorMapper(AppointmentDB.class)
    List<AppointmentDB> getAppointmentsConfirmedForTodayOrTomorrowFor(long chatId);

    @SqlQuery("select exists (select appointment_id from appointments where room=:roomName and chat_id =:chatId and event_start =:eventStart) as record_exists;")
    @RegisterConstructorMapper(AppointmentDB.class)
    boolean isAppointmentExist(@NotNull String roomName, long chatId, @NotNull Timestamp eventStart);

    @SqlUpdate("update appointments set status =:newStatus where chat_id =:chatId and event_start =:eventStartDate;")
    void updateAppointmentStatusWith(boolean newStatus, long chatId, Timestamp eventStartDate);

    @SqlUpdate("update appointments set image_id =:imageId where appointment_id=:appId;")
    void updateAppointmentImageIdFor(String imageId, int appId);

    @SqlUpdate("delete from appointments where room like :value;")
    void deleteAppointmentWithRoomNameContains(@NotNull String value);
}




