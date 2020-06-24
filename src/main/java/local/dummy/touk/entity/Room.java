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
public class Room extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_generator")
	@SequenceGenerator(name = "room_generator", sequenceName = "room_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "room")
	private Set<Screening> screenings = new LinkedHashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private Set<Row> rows = new LinkedHashSet<>();

	public Room(String name) {
		this.name = name;
	}

	public void addRow(Row row) {
		rows.add(row);
	}

	@Override
	public String toString() {
		return "Room{" +
				"name='" + name + '\'' +
				'}';
	}
}
