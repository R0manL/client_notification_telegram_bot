package org.grow.db.pojo;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class AppointmentDB {
    int appointmentId;
    @NotNull
    String room;
    @NotNull
    Timestamp eventStart;
    long chatId;
    Boolean status;
    String imageId;
    int therapistId;
    Timestamp createDate;
    Timestamp updateDate;
}
