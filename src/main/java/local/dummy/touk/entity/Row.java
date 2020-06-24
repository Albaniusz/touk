package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seat_row")
public class Row extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "row_generator")
	@SequenceGenerator(name = "row_generator", sequenceName = "row_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "row")
	private Set<Seat> seats = new LinkedHashSet<>();

	public Row(String name, Room room) {
		this.name = name;
		this.room = room;
	}

	public void addSeat(Seat seat) {
		seats.add(seat);
	}
}
