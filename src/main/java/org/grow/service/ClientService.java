package org.grow.service;

import lombok.extern.log4j.Log4j2;
import org.grow.db.service.ClientDBService;

import static org.grow.db.service.ClientDBService.insertClient;
import static org.grow.utils.StringUtils.formatPhoneNumberForDB;


@Log4j2
public class ClientService {

    public void addNewClient(long chatId, String phoneNumber) {
        String phone = formatPhoneNumberForDB(phoneNumber);
        insertClient(chatId, phone, true);
    }

    public void updateClient(int clientId, String newPhoneNumber) {
        String phone = formatPhoneNumberForDB(newPhoneNumber);
        ClientDBService.updateClient(clientId, phone, true);
    }
}
