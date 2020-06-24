package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_generator")
	@SequenceGenerator(name = "ticket_generator", sequenceName = "ticket_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "bought_time")
	private ZonedDateTime boughtTime;

	@ManyToOne
	@JoinColumn(name = "kind_of_ticket_id")
	private KindOfTicket kindOfTicket;

	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	@ManyToOne
	@JoinColumn(name = "screening_id")
	private Screening screening;

	@ManyToOne
	@JoinColumn(name = "seat_id")
	private Seat seat;

	public Ticket(Reservation reservation, Screening screening, Seat seat, KindOfTicket kindOfTicket) {
		boughtTime = ZonedDateTime.now();
		this.reservation = reservation;
		this.screening = screening;
		this.seat = seat;
		this.kindOfTicket = kindOfTicket;
	}

	@Override
	public String toString() {
		return "Ticket{" +
				"boughtTime=" + boughtTime +
				'}';
	}
}
