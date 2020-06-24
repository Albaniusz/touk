package local.dummy.touk.dto;

import local.dummy.touk.entity.Movie;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MovieDTO extends RepresentationModel<MovieDTO> {
	private Long id;

	private String title;

	private String ageRestriction;

	private LocalDate datePremiere;

	private LocalDate onOfferFrom;

	private LocalDate onOfferTo;

	private Short length;

	private List<ScreeningDTO> screenings;

	public MovieDTO(Movie movie) {
		id = movie.getId();
		title = movie.getTitle();
		ageRestriction = movie.getAgeRestriction();
		datePremiere = movie.getDatePremiere();
		onOfferFrom = movie.getOnOfferFrom();
		onOfferTo = movie.getOnOfferTo();
		length = movie.getLength();
	}
}
