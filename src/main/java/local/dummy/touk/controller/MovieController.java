package local.dummy.touk.controller;

import local.dummy.touk.dto.MovieDTO;
import local.dummy.touk.dto.ScreeningDTO;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
public class MovieController {
	private final static Logger logger = LoggerFactory.getLogger(MovieController.class);

	private MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping("/list/{dateTime}")
	public ResponseEntity list(@PathVariable("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
									   ZonedDateTime dateTimeStartSearching) throws ScreeningException {
		logger.debug("Action: list with dateTime {}", dateTimeStartSearching);

		List<MovieDTO> movies = movieService.getMoviesByDateTime(dateTimeStartSearching).stream()
				.map(movie -> {
					MovieDTO movieDTO = new MovieDTO(movie);
					movieDTO.setScreenings(movie.getScreenings().stream()
							.map(ScreeningDTO::new)
							.collect(Collectors.toList()));
					return movieDTO;
				})
				.collect(Collectors.toList());

		return new ResponseEntity<>(movies, HttpStatus.OK);
	}
}
