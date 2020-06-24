package local.dummy.touk.service;

import local.dummy.touk.dto.ReservationDTO;
import local.dummy.touk.entity.*;
import local.dummy.touk.exception.ReservationException;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.form.MakeReservationForm;
import local.dummy.touk.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private KindOfTicketRepository kindOfTicketRepository;

    @Mock
    private RowRepository rowRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private final ReservationService reservationService = new ReservationServiceImpl(screeningRepository, seatRepository,
            reservationRepository, kindOfTicketRepository, rowRepository, ticketRepository);

    @Test
    public void makeReservation_shouldFoundNoScreening() {
        // given
        MakeReservationForm makeReservationForm = configureForm(-1L, null, null);

        // when
        // then
        assertThrows(ScreeningException.class, () -> {
            reservationService.makeReservation(makeReservationForm);
        });
    }

    @Test
    public void makeReservation_shouldTooLateToMakeReservation() throws Exception {
        // given
        long screeningId = 1L;

        MakeReservationForm makeReservationForm = configureForm(screeningId, null, null);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now());

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        // when
        // then
        assertThrows(ReservationException.class, () -> {
            reservationService.makeReservation(makeReservationForm);
        });
    }

    @Test
    public void makeReservation_shouldFindSeatIsNotInScreening() throws Exception {
        // given
        long screeningId = 1L;

        MakeReservationForm makeReservationForm = configureForm(screeningId, Collections.singletonList(-1L), null);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        // when
        // then
        assertThrows(ReservationException.class, () -> reservationService.makeReservation(makeReservationForm));
    }

    @Test
    public void makeReservation_shouldFindSeatIsOccupied() throws Exception {
        // given
        long screeningId = 1L;
        long seatIdA = 1L;

        MakeReservationForm makeReservationForm = configureForm(screeningId, Collections.singletonList(seatIdA), null);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        Seat seatA = new Seat();
        seatA.setId(seatIdA);
        seatA.addTicket(new Ticket());

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        when(seatRepository.findSeatingInScreening(screeningId, seatIdA))
                .thenReturn(Optional.of(seatA));

        // when
        // then
        assertThrows(ReservationException.class, () -> reservationService.makeReservation(makeReservationForm));
    }

    @Test
    public void makeReservation_shouldFindNotOccupiedSeatsBetween() throws Exception {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        long seatToReserveAId = 1L;
        long seatToReserveBId = 3L;
        long seatEmptyId = 3L;
        long kindOfTicketId = 1L;

        List<Long> seatsIds = Arrays.asList(seatToReserveAId, seatToReserveBId);

        MakeReservationForm makeReservationForm = configureForm(screeningId, seatsIds, kindOfTicketId);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        Row row = new Row();
        row.setId(rowId);

        Seat seatToReserveA = configureSeat(row, seatToReserveAId, (short) 1);
        row.addSeat(seatToReserveA);

        Seat emptySeat = configureSeat(row, seatEmptyId, (short) 2);
        row.addSeat(emptySeat);

        Seat seatToReserveB = configureSeat(row, seatToReserveBId, (short) 3);
        row.addSeat(seatToReserveB);

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        when(seatRepository.findSeatingInScreening(screeningId, seatToReserveAId))
                .thenReturn(Optional.of(seatToReserveA));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        // when
        // then
        assertThrows(ReservationException.class, () -> reservationService.makeReservation(makeReservationForm));
    }

    public void makeReservation_shouldMakeReservationForOneSeat() throws Exception {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        long seatToReserveAId = 1L;
        long kindOfTicketId = 1L;

        List<Long> seatsIds = Collections.singletonList(seatToReserveAId);

        MakeReservationForm makeReservationForm = configureForm(screeningId, seatsIds, kindOfTicketId);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        Row row = new Row();
        row.setId(rowId);

        Seat seatToReserveA = configureSeat(row, seatToReserveAId, (short) 1);
        row.addSeat(seatToReserveA);

        Seat emptySeat = configureSeat(row, 2, (short) 2);
        row.addSeat(emptySeat);

        Seat seatToReserveB = configureSeat(row, 3, (short) 3);
        row.addSeat(seatToReserveB);

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        when(seatRepository.findSeatingInScreening(screeningId, seatToReserveAId))
                .thenReturn(Optional.of(seatToReserveA));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        KindOfTicket kindOfTicket = new KindOfTicket();
        kindOfTicket.setPrice(new BigDecimal(1));
        when(kindOfTicketRepository.getOne(kindOfTicketId))
                .thenReturn(kindOfTicket);

        // when
        ReservationDTO reservationDTO = reservationService.makeReservation(makeReservationForm);

        // then
        assertEquals(new BigDecimal(1), reservationDTO.getAmount());
    }

    public void makeReservation_shouldMakeReservationForTwoSeats() throws Exception {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        long seatToReserveAId = 1L;
        long kindOfTicketId = 1L;

        List<Long> seatsIds = Collections.singletonList(seatToReserveAId);

        MakeReservationForm makeReservationForm = configureForm(screeningId, seatsIds, kindOfTicketId);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        Row row = new Row();
        row.setId(rowId);

        Seat seatToReserveA = configureSeat(row, seatToReserveAId, (short) 1);
        row.addSeat(seatToReserveA);

        Seat emptySeat = configureSeat(row, 2, (short) 2);
        row.addSeat(emptySeat);

        Seat seatToReserveB = configureSeat(row, 3, (short) 3);
        row.addSeat(seatToReserveB);

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        when(seatRepository.findSeatingInScreening(screeningId, seatToReserveAId))
                .thenReturn(Optional.of(seatToReserveA));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        KindOfTicket kindOfTicket = new KindOfTicket();
        kindOfTicket.setPrice(new BigDecimal(1));
        when(kindOfTicketRepository.getOne(kindOfTicketId))
                .thenReturn(kindOfTicket);

        // when
        ReservationDTO reservationDTO = reservationService.makeReservation(makeReservationForm);

        // then
        assertEquals(new BigDecimal(1), reservationDTO.getAmount());
    }

    public void makeReservation_shouldFindTicketKindNotExists() throws Exception {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        long seatToReserveAId = 1L;
        long seatToReserveBId = 2L;
        long kindOfTicketId = 1L;

        List<Long> seatsIds = Arrays.asList(seatToReserveAId, seatToReserveBId);

        MakeReservationForm makeReservationForm = configureForm(screeningId, seatsIds, kindOfTicketId);

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(2));

        Row row = new Row();
        row.setId(rowId);

        Seat seatToReserveA = configureSeat(row, seatToReserveAId, (short) 1);
        row.addSeat(seatToReserveA);

        Seat emptySeat = configureSeat(row, 2, (short) 2);
        row.addSeat(emptySeat);

        Seat seatToReserveB = configureSeat(row, 3, (short) 3);
        row.addSeat(seatToReserveB);

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        when(seatRepository.findSeatingInScreening(screeningId, seatToReserveAId))
                .thenReturn(Optional.of(seatToReserveA));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        when(kindOfTicketRepository.findById(kindOfTicketId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThrows(ReservationException.class, () -> reservationService.makeReservation(makeReservationForm));
    }

    @Test
    public void checkIsNotTooLateToMakeReservation_shouldCheckTooLateToMakeReservation() throws Exception {
        // given
        long screeningId = 1L;

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now());

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        // when
        // then
        assertFalse(reservationService.checkIsNotTooLateToMakeReservation(screeningId));
    }

    @Test
    public void checkIsNotTooLateToMakeReservation_shouldCheckNotToLateToMakeReservation() throws Exception {
        // given
        long screeningId = 1L;

        Screening screening = new Screening();
        screening.setId(screeningId);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(1));

        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        // when
        // then
        assertTrue(reservationService.checkIsNotTooLateToMakeReservation(screeningId));
    }

    @Test
    public void checkIsSeatBetweenSeatsNotOccupied_shouldNotFoundRow() {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        List<Long> seatsIds = Arrays.asList(1L, 3L);

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThrows(ReservationException.class, () -> reservationService.checkIsSeatBetweenSeatsNotOccupied(screeningId, rowId, seatsIds));
    }

    @Test
    public void checkIsSeatBetweenSeatsNotOccupied_shouldCheckNotOccupiedSeatsBetween() throws ReservationException {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        List<Long> seatsIds = Arrays.asList(1L, 3L);

        Row row = new Row();
        row.setId(rowId);
        row.setName("A");

        row.addSeat(configureSeat(row, 1L));
        row.addSeat(configureSeat(row, 2L));
        row.addSeat(configureSeat(row, 3L));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        // when
        // then
        assertTrue(reservationService.checkIsSeatBetweenSeatsNotOccupied(screeningId, rowId, seatsIds));
    }

    @Test
    public void checkIsSeatBetweenSeatsNotOccupied_shouldCheckNonNotOccupiedSeatsBetween() throws ReservationException {
        // given
        long screeningId = 1L;
        long rowId = 1L;
        List<Long> seatsIds = Arrays.asList(1L, 3L);

        Row row = new Row();
        row.setId(rowId);
        row.setName("A");

        row.addSeat(configureSeat(row, 1L));

        Seat seatWithTicket = configureSeat(row, 2L);
        seatWithTicket.addTicket(new Ticket());
        row.addSeat(seatWithTicket);

        row.addSeat(configureSeat(row, 3L));

        when(rowRepository.findRowInScreeningWithSeats(screeningId, rowId))
                .thenReturn(Optional.of(row));

        // when
        // then
        assertFalse(reservationService.checkIsSeatBetweenSeatsNotOccupied(screeningId, rowId, seatsIds));
    }

    private Seat configureSeat(Row row, long seatNum) {
        return configureSeat(row, seatNum, (short) seatNum);
    }

    private Seat configureSeat(Row row, long seatId, short seatNumber) {
        Seat seat = new Seat();

        seat.setId(seatId);
        seat.setSeatNumber(seatNumber);
        seat.setRow(row);

        return seat;
    }

    private MakeReservationForm configureForm(Long screeningId, List<Long> seatsIds, Long kindOfTicketId) {
        MakeReservationForm makeReservationForm = new MakeReservationForm();

        makeReservationForm.setScreeningId(screeningId);
        makeReservationForm.setSeatsIds(seatsIds);
        makeReservationForm.setKindOfTicketId(kindOfTicketId);

        return makeReservationForm;
    }
}
