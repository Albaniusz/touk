package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Seat extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_generator")
	@SequenceGenerator(name = "seat_generator", sequenceName = "seat_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "seat_number")
	private Short seatNumber;

	@ManyToOne
	@JoinColumn(name = "row_id")
	private Row row;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "seat")
	private Set<Ticket> tickets = new LinkedHashSet<>();

	public Seat(Short seatNumber, Row row) {
		this.seatNumber = seatNumber;
		this.row = row;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	@Override
	public String toString() {
		return "Seat{" +
				"seatNumber=" + seatNumber +
				'}';
	}
}
