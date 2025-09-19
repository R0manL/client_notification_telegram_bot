package org.grow.db.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.grow.db.DBConnection;
import org.grow.db.pojo.ClientDB;
import org.grow.db.dao.ClientDAO;

import java.util.List;
import java.util.Optional;

@Log4j2
public class ClientDBService {

    private ClientDBService() {
        // NONE
    }


    public static Optional<Long> getChatIdForClientThatContains(@NotNull String phoneNum) {
        phoneNum = phoneNum.replaceAll("\\s+", "");
        log.debug("(DB) Get chatID by phone number: '{}'", phoneNum);

        Long result = DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .getChatIdForClientThatContains(phoneNum);

        log.debug("(DB) Found chatID: {}", result);

        return Optional.ofNullable(result);
    }

    public static List<String> getPhoneNumbersForAllUserThatAre(boolean active) {
        log.debug("(DB) Getting all phone numbers for user active={}", active);
        return DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .getPhoneNumbersForAllUserThatAre(active);
    }

    @Nullable
    public static ClientDB getClientBy(long chatId) {
        log.debug("(DB) Getting client by chatID: {}", chatId);
        return DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .getClientBy(chatId);
    }

    /**
     * Method update client's is_active parameter with a new value
     * @param isActive - value to set
     * @param chatId  - client identified by chatId
     */
    public static void setClientActivityTo(boolean isActive, long chatId) {
        log.debug("(DB) Set client: '{}' activity to: {}", chatId, isActive);
        DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .setClientActivityTo(isActive, chatId);
    }

    public static void insertClient(long chatId, @NotNull String phone, boolean isActive) {
        log.debug("(DB) insert a new client with chatId/phone/isActive: '{}', '{}', '{}'.", chatId, phone, isActive);

        DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .insertClient(chatId, phone, isActive);
    }

    public static void updateClient(int clientId, @NotNull String newPhoneNumber, boolean isActive) {
        log.debug("(DB) update client: '{}' with a new phone number: '{}', set isActive to '{}'.", clientId, newPhoneNumber, isActive);

        DBConnection.createConnection()
                .onDemand(ClientDAO.class)
                .updateClient(clientId, newPhoneNumber, isActive);
    }
}
