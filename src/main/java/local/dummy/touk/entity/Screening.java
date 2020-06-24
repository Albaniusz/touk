package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Screening extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_generator")
	@SequenceGenerator(name = "screening_generator", sequenceName = "screening_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "screening_time")
	private ZonedDateTime screeningTime;

	@ManyToOne
	@JoinColumn(name = "movie_id")
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	@OneToMany(mappedBy = "screening")
	private Set<Ticket> tickets = new LinkedHashSet<>();

	public Screening(Room room, Movie movie, ZonedDateTime screeningTime) {
		this.room = room;
		this.movie = movie;
		this.screeningTime = screeningTime;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	@Override
	public String toString() {
		return "Screening{" +
				"id=" + id +
				", screeningTime=" + screeningTime +
				'}';
	}
}
