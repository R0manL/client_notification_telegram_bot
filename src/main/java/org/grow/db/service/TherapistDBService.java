package org.grow.db.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.grow.db.dao.TherapistDAO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.grow.db.DBConnection.createConnection;

@Log4j2
public class TherapistDBService {

    private TherapistDBService() {
        // NONE
    }

    public static Optional<Integer> getTherapistIdBy(@NotNull String lastName) {
        log.debug("(DB) Get therapist ID by last name: '{}'", lastName);
        Integer result = createConnection()
                .onDemand(TherapistDAO.class)
                .getTherapistIdBy(lastName);

        return Optional.ofNullable(result);
    }

    @NotNull
    public static String getTherapistFirstNameBy(int id) {
        log.debug("(DB) Get therapist's first name by ID: '{}'", id);
        return createConnection()
                .onDemand(TherapistDAO.class)
                .getTherapistFirstNameBy(id);
    }

    @NotNull
    public static String getTherapistLastNameBy(int id) {
        log.debug("(DB) Get therapist's last name by ID: '{}'", id);
        return createConnection()
                .onDemand(TherapistDAO.class)
                .getTherapistLastNameBy(id);
    }

    @NotNull
    public static String getTherapistIBANBy(int id) {
        log.debug("(DB) Get therapist's IBAN by ID: '{}'", id);
        return createConnection()
                .onDemand(TherapistDAO.class)
                .getTherapistIBANBy(id);
    }

    @NotNull
    public static Optional<String> getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor(long chatId) {
        log.debug("(DB) Get therapist's IBAN for last confirmed appointment (today or tomorrow) for chatId: '{}'", chatId);
        List<String> ibans = createConnection()
                .onDemand(TherapistDAO.class)
                .getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor(chatId)
                .stream()
                .filter(Objects::nonNull)
                .toList();

        int numOfIbans = ibans.size();

        if (numOfIbans > 1) { log.warn("Expect single iban, but found: " + numOfIbans + ", return last one."); }

        return ibans.isEmpty() ? Optional.empty() : Optional.of(ibans.get(numOfIbans - 1));
    }
}
