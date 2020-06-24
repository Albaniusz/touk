package local.dummy.touk;

import local.dummy.touk.entity.*;
import local.dummy.touk.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private KindOfTicketRepository kindOfTicketRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ScreeningRepository screeningRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private TicketRepository ticketRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initPersistenceData();
	}

	private void initPersistenceData() throws Exception {
		logger.debug("Filling persistence with dummy data");

		// generating kind of tickets
		KindOfTicket kindOfTicketAdult = new KindOfTicket("adult", BigDecimal.valueOf(25.0));
		kindOfTicketRepository.save(kindOfTicketAdult);
		KindOfTicket kindOfTicketStudent = new KindOfTicket("student", BigDecimal.valueOf(18.0));
		kindOfTicketRepository.save(kindOfTicketStudent);
		KindOfTicket kindOfTicketChild = new KindOfTicket("child", BigDecimal.valueOf(12.5));
		kindOfTicketRepository.save(kindOfTicketChild);

		// generating screening rooms with rows and seats
		Room roomA = new Room("Room A");
		roomA = fillRoomWithSitesHelper(roomA, 4, 5);
		roomRepository.save(roomA);
		logger.debug("RoomA: {} ", roomA);

		Room roomB = new Room("Room B");
		roomB = fillRoomWithSitesHelper(roomB, 6, 6);
		roomRepository.save(roomB);
		logger.debug("RoomB: {} ", roomB);

		Room roomC = new Room("Room C");
		roomC = fillRoomWithSitesHelper(roomC, 5, 5);
		roomRepository.save(roomC);
		logger.debug("RoomC: {} ", roomC);

		// generating movies
		// use ISO_LOCAL_DATE: 2016-08-16
		Movie movie1 = new Movie("Super Mega Hit",
				(short) 123,
				LocalDate.parse("2020-02-10"),
				LocalDate.parse("2020-02-15"),
				LocalDate.parse("2020-03-15")
		);
		movieRepository.save(movie1);

		Movie movie2 = new Movie("Movie 2",
				(short) 90,
				LocalDate.parse("2020-02-01"),
				LocalDate.parse("2020-02-15"),
				LocalDate.parse("2020-03-15")
		);
		movieRepository.save(movie2);

		Movie movie3 = new Movie("Movie 3",
				(short) 100,
				LocalDate.parse("2020-02-17"),
				LocalDate.parse("2020-02-15"),
				LocalDate.parse("2020-03-15")
		);
		movieRepository.save(movie3);

		Movie movie4 = new Movie("Movie 4",
				(short) 116,
				LocalDate.parse("2020-02-21"),
				LocalDate.parse("2020-02-25"),
				LocalDate.parse("2020-03-25")
		);
		movieRepository.save(movie4);

		Movie movie5 = new Movie("Movie 5",
				(short) 116,
				LocalDate.parse("2020-02-22"),
				LocalDate.parse("2020-02-23"),
				LocalDate.parse("2020-03-23")
		);
		movieRepository.save(movie5);

		Movie movie6 = new Movie("Bad Taste",
				(short) 91,
				LocalDate.parse("1987-12-01"),
				LocalDate.parse("2020-02-15"),
				LocalDate.parse("2020-03-15")
		);
		movieRepository.save(movie6);

		// generating screenings
		Screening screeningA1 = new Screening(roomA, movie1, generateShowingDateTimeHelper("12:00"));
		screeningRepository.save(screeningA1);
		Screening screeningA2 = new Screening(roomA, movie1, generateShowingDateTimeHelper("14:30"));
		screeningRepository.save(screeningA2);
		Screening screeningA3 = new Screening(roomA, movie1, generateShowingDateTimeHelper("17:00"));
		screeningRepository.save(screeningA3);
		Screening screeningA4 = new Screening(roomA, movie1, generateShowingDateTimeHelper("19:30"));
		screeningRepository.save(screeningA4);

		Screening screeningB1 = new Screening(roomB, movie2, generateShowingDateTimeHelper("15:30"));
		screeningRepository.save(screeningB1);
		Screening screeningB2 = new Screening(roomB, movie3, generateShowingDateTimeHelper("17:30"));
		screeningRepository.save(screeningB2);
		Screening screeningB3 = new Screening(roomB, movie2, generateShowingDateTimeHelper("19:30"));
		screeningRepository.save(screeningB3);
		Screening screeningB4 = new Screening(roomB, movie3, generateShowingDateTimeHelper("21:30"));
		screeningRepository.save(screeningB4);

		Screening screeningC1 = new Screening(roomC, movie4, generateShowingDateTimeHelper("13:00"));
		screeningRepository.save(screeningC1);
		Screening screeningC2 = new Screening(roomC, movie5, generateShowingDateTimeHelper("15:00"));
		screeningRepository.save(screeningC2);
		Screening screeningC3 = new Screening(roomC, movie4, generateShowingDateTimeHelper("17:00"));
		screeningRepository.save(screeningC3);
		Screening screeningC4 = new Screening(roomC, movie5, generateShowingDateTimeHelper("19:00"));
		screeningRepository.save(screeningC4);
		Screening screeningC5 = new Screening(roomC, movie6, generateShowingDateTimeHelper("21:00"));
		screeningRepository.save(screeningC5);

		// dummy ticket
		KindOfTicket kindOfTicket = kindOfTicketRepository.getOne(1L);

		Ticket ticket1 = new Ticket();
		ticket1.setKindOfTicket(kindOfTicket);
		ticket1.setBoughtTime(ZonedDateTime.now());
		ticket1.setScreening(screeningA3);
		ticket1.setSeat(seatRepository.getOne(1L));
		ticketRepository.save(ticket1);

		Ticket ticket2 = new Ticket();
		ticket2.setKindOfTicket(kindOfTicket);
		ticket2.setBoughtTime(ZonedDateTime.now());
		ticket2.setScreening(screeningA3);
		ticket2.setSeat(seatRepository.getOne(2L));
		ticketRepository.save(ticket2);
	}

	/**
	 * Fill room with rows filled with seats
	 *
	 * @param room      room to fill with data
	 * @param rows      number of rows in room
	 * @param seatInRow number of seats in fow
	 * @return room filled with rows and seats
	 */
	private Room fillRoomWithSitesHelper(Room room, int rows, int seatInRow) {
		char startLetter = 'A';
		char limit = (char) ((int) startLetter + rows);

		for (char letter = startLetter; letter < limit; letter++) {
			Row row = new Row("" + letter, room);
			for (int x = 1; x <= seatInRow; x++) {
				Seat seat = new Seat((short) x, row);
				row.addSeat(seat);
			}
			room.addRow(row);
		}

		return room;
	}

	private ZonedDateTime generateShowingDateTimeHelper(String hour) {
		return ZonedDateTime.parse(String.format("2020-07-05T%s:00Z", hour));
	}
}
