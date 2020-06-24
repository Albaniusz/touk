package local.dummy.touk.service;

import local.dummy.touk.entity.Movie;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
	private final static Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

	private MovieRepository movieRepository;

	public MovieServiceImpl(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	@Transactional
	public List<Movie> getMoviesByDateTime(ZonedDateTime dateTimeFrom) throws ScreeningException {
		logger.debug("Start searching for screenings by dateTime {}", dateTimeFrom);

		if (ZonedDateTime.now().isAfter(dateTimeFrom)) {
			logger.debug("Screening search date {} is past", dateTimeFrom);
			throw new ScreeningException("Screening search date is past");
		}

		if (ZonedDateTime.now().plusDays(14).isBefore(dateTimeFrom)) {
			logger.debug("Screening search date {} is too far", dateTimeFrom);
			throw new ScreeningException("Screening search date is too far");
		}

		ZonedDateTime dateTimeTo = dateTimeFrom.plusHours(8);

		logger.debug("Searching movies with dateTime criteria {}-{}", dateTimeFrom, dateTimeTo);
		List<Movie> movies = movieRepository.getMoviesByQueryWithDate(dateTimeFrom, dateTimeTo);

		if (movies.isEmpty()) {
			logger.debug("No movies and screenings found with searching dateTime {}-{}", dateTimeFrom, dateTimeTo);
			throw new ScreeningException("No movies and screenings found with searching dateTime");
		}

		logger.debug("Found movies with dateTime criteria {}-{} {}", dateTimeFrom, dateTimeTo, movies);

		return movies;
	}
}
