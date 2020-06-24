package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kind_of_ticket")
public class KindOfTicket extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kind_of_ticket_generator")
	@SequenceGenerator(name = "kind_of_ticket_generator", sequenceName = "kind_of_ticket_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(length = 16)
	private String name;

	private BigDecimal price;

	@OneToMany(mappedBy = "kindOfTicket")
	Set<Ticket> tickets = new LinkedHashSet<>();

	public KindOfTicket(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	@Override
	public String toString() {
		return "KindOfTicket{" +
				"name='" + name + '\'' +
				", price=" + price +
				'}';
	}
}
