package org.grow.db.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ClientDB {
    private int clientId;
    private long chatId;
    @NotNull
    private String phone;
    private boolean isActive;
    private Timestamp createDate;
    private Timestamp updateDate;
}
