package local.dummy.touk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie extends BaseEntity {
	@Id
	@GeneratedValue
	@SequenceGenerator(name = "movie_generator", sequenceName = "movie_seq")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	private String title;

	@Column(name = "age_restriction")
	private String ageRestriction;

	@Column(name = "date_premiere")
	private LocalDate datePremiere;

	/**
	 * In cinema offer from date
	 */
	@Column(name = "on_offer_from")
	private LocalDate onOfferFrom;

	/**
	 * In cinema offer to date
	 */
	@Column(name = "on_offer_to")
	private LocalDate onOfferTo;

	/**
	 * Movie length in minutes
	 */
	private Short length;

	@OneToMany(mappedBy = "movie")
	private Set<Screening> screenings = new LinkedHashSet<>();

	public Movie(String title, short length, LocalDate datePremiere, LocalDate onOfferFrom, LocalDate onOfferTo) {
		this.title = title;
		this.length = length;
		this.datePremiere = datePremiere;
		this.onOfferFrom = onOfferFrom;
		this.onOfferTo = onOfferTo;
	}

	public void addScreening(Screening screening) {
		screenings.add(screening);
	}

	@Override
	public String toString() {
		return "Movie{" +
				"title='" + title + '\'' +
				", ageRestriction='" + ageRestriction + '\'' +
				", datePremiere=" + datePremiere +
				", onOfferFrom=" + onOfferFrom +
				", onOfferTo=" + onOfferTo +
				'}';
	}
}
