package org.grow.db.dao;


import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface TherapistDAO {

    @Nullable
    @SqlQuery("select therapist_id from therapists where last_name=:lastName;")
    Integer getTherapistIdBy(@NotNull String lastName);

    @SqlQuery("select first_name from therapists where therapist_id=:id;")
    String getTherapistFirstNameBy(long id);

    @SqlQuery("select last_name from therapists where therapist_id=:id;")
    String getTherapistLastNameBy(long id);

    @SqlQuery("select iban from therapists where therapist_id=:id;")
    String getTherapistIBANBy(long id);

    @SqlQuery("SELECT therapists.iban FROM therapists\n JOIN appointments ON therapists.therapist_id = appointments.therapist_id WHERE chat_id = :chatId AND status = TRUE AND (DATE(event_start) = CURRENT_DATE OR DATE(event_start) = ( CURRENT_DATE + INTERVAL 1 DAY));")
    List<String> getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor(long chatId);
}




