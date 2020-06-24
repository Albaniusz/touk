package local.dummy.touk.service;

import local.dummy.touk.dto.ReservationDTO;
import local.dummy.touk.entity.*;
import local.dummy.touk.exception.ReservationException;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.form.MakeReservationForm;
import local.dummy.touk.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private ScreeningRepository screeningRepository;

    private SeatRepository seatRepository;

    private ReservationRepository reservationRepository;

    private KindOfTicketRepository kindOfTicketRepository;

    private RowRepository rowRepository;

    private TicketRepository ticketRepository;

    @Value("${cinema.reservation.expirationBeforeScreeningInMinutes}")
    private short expirationBeforeScreeningInMinutes;

    public ReservationServiceImpl(ScreeningRepository screeningRepository, SeatRepository seatRepository, ReservationRepository reservationRepository, KindOfTicketRepository kindOfTicketRepository, RowRepository rowRepository, TicketRepository ticketRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
        this.kindOfTicketRepository = kindOfTicketRepository;
        this.rowRepository = rowRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public ReservationDTO makeReservation(MakeReservationForm makeReservationForm) throws Exception {
        logger.debug("Checking screening ID");
        long screeningId = makeReservationForm.getScreeningId();
        Screening screening = screeningRepository.getScreeningByQueryWithId(makeReservationForm.getScreeningId())
                .orElseThrow(() -> {
                    logger.error("No screening with ID {}", makeReservationForm.getScreeningId());
                    return new ScreeningException("No screening with ID " + makeReservationForm.getScreeningId());
                });

        if (!checkIsNotTooLateToMakeReservation(screening.getId())) {
            throw new ReservationException("It is too late to make reservation");
        }

        logger.debug("Prepare reservation");
        Reservation reservation = new Reservation(
                makeReservationForm.getName(),
                makeReservationForm.getSurname()
        );

        logger.debug("Prepare tickets");
        List<Long> seatsIds = makeReservationForm.getSeatsIds();
        BigDecimal amount = new BigDecimal(0);
        for (Long seatId : seatsIds) {
            logger.debug("Validating seat with id {}", seatId);
            Seat seat = seatRepository.findSeatingInScreening(screeningId, seatId)
                    .orElseThrow(() -> {
                        logger.debug("Seat {} in screening {} not exists", seatId, screeningId);
                        return new ReservationException("Seat " + seatId + " in screening " + screeningId + " not exists");
                    });

            logger.debug("Checking seat is not occupied");
            if (seat.getTickets().size() > 0) {
                logger.debug("Seat {} is occupied", seatId);
                throw new ReservationException("Seat " + seatId + " is occupied");
            }

            if (checkIsSeatBetweenSeatsNotOccupied(screeningId, seat.getRow().getId(), seatsIds)) {
                logger.debug("Found empty seat between seats with ticket");
                throw new ReservationException("Found empty seat between seats with ticket");
            }

            logger.debug("Generating ticket");
            long kindOfTicketId = makeReservationForm.getKindOfTicketId();
            KindOfTicket kindOfTicket = kindOfTicketRepository.findById(kindOfTicketId).orElseThrow(() -> {
                logger.debug("Kind of ticket {} not found", kindOfTicketId);
                return new ReservationException("Kind of ticket " + kindOfTicketId + " not found");
            });
            Ticket ticket = new Ticket(reservation, screening, seat, kindOfTicket);
            reservation.addTicket(ticket);
            screening.addTicket(ticket);
            seat.addTicket(ticket);
            kindOfTicket.addTicket(ticket);
            ticketRepository.save(ticket);

            logger.debug("Saving reservation");
            reservationRepository.save(reservation);

            amount = amount.add(kindOfTicket.getPrice());
        }

        logger.debug("Prepare response");

        ReservationDTO response = new ReservationDTO();
        response.setAmount(amount);
        response.setReservationExpirationDate(screening.getScreeningTime().minusMinutes(expirationBeforeScreeningInMinutes));

        return response;
    }

    @Override
    @Transactional
    public boolean checkIsNotTooLateToMakeReservation(long screeningId) throws Exception {
        logger.debug("Checking it is not too late to make reservation");

        Screening screening = screeningRepository.getScreeningByQueryWithId(screeningId)
                .orElseThrow(() -> {
                    logger.error("No screening with ID {}", screeningId);
                    return new ScreeningException("No screening with ID " + screeningId);
                });

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime reservationExpirationDateTime = screening.getScreeningTime().minusMinutes(expirationBeforeScreeningInMinutes);

        if (now.isAfter(reservationExpirationDateTime)) {
            logger.debug("Too late {} to make reservation for screening {} {}", now, screeningId,
                    reservationExpirationDateTime);
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean checkIsSeatBetweenSeatsNotOccupied(long screeningId, long rowId, List<Long> seatsIds) throws ReservationException {
        logger.debug("Looking for space (empty seat) between seats with tickets");

        Row row = rowRepository.findRowInScreeningWithSeats(screeningId, rowId)
                .orElseThrow(() -> {
                    logger.debug("Row {} not found", rowId);
                    return new ReservationException("Row " + rowId + " not found");
                });

        List<Seat> filteredSeatsList = row.getSeats().stream()
                .sorted(Comparator.comparing(Seat::getSeatNumber))
                .filter(s -> s.getTickets().size() > 0 || seatsIds.contains(s.getId()))
                .collect(Collectors.toList());

        Seat lastOneSeatInRow = null;
        for (Seat seatInRow : filteredSeatsList) {
            if (lastOneSeatInRow != null && seatInRow.getSeatNumber() - lastOneSeatInRow.getSeatNumber() > 1) {
                logger.debug("Found empty seat between seats with ticket");
                return true;
            }
            lastOneSeatInRow = seatInRow;
        }

        logger.debug("Not found empty seat between seats with ticket");
        return false;
    }
}
