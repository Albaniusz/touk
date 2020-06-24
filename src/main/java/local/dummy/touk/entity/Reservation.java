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
public class Reservation extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_generator")
	@SequenceGenerator(name = "reservation_generator", sequenceName = "reservation_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	private String name;

	private String surname;

	@OneToMany(mappedBy = "reservation")
	Set<Ticket> tickets = new LinkedHashSet<>();

	public Reservation(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
