package org.grow.csv.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class AppointmentCSV {
    @NotNull
    String room;
    @NotNull
    String eventStart;
    @NotNull
    String clientPhone;
    @NotNull
    String therapist;
    String bot;
    String isNew;
    String isOnline;
    String price;
    String hasPaid;
    String comeFrom;
    String comment;
}
