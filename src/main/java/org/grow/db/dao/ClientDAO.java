package org.grow.db.dao;


import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.grow.db.pojo.ClientDB;

import java.util.List;


public interface ClientDAO {

    @Nullable
    @SqlQuery("select chat_id from clients where phone like CONCAT('%', :phoneNum , '%')")
    Long getChatIdForClientThatContains(@NotNull String phoneNum);

    @SqlQuery("select phone from clients where is_active =:isActive;")
    List<String> getPhoneNumbersForAllUserThatAre(boolean isActive);

    @SqlQuery("select * from clients where chat_id =:chatId;")
    @RegisterConstructorMapper(ClientDB.class)
    ClientDB getClientBy(long chatId);

    @SqlUpdate("update clients set is_active =:isActive where chat_id =:chatId;")
    void setClientActivityTo(boolean isActive, long chatId);

    @SqlUpdate("update clients set is_active =:isActive, phone =:phoneNum where client_id =:clientId;")
    void updateClient(int clientId, String phoneNum, boolean isActive);

    @SqlUpdate("insert into clients (chat_id, phone, is_active) VALUES (:chatId, :phone, :isActive);")
    int insertClient(long chatId, @NotNull String phone, boolean isActive);
}




