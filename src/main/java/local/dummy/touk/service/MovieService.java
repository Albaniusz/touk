package local.dummy.touk.service;

import local.dummy.touk.entity.Movie;
import local.dummy.touk.exception.ScreeningException;

import java.time.ZonedDateTime;
import java.util.List;

public interface MovieService {
	List<Movie> getMoviesByDateTime(ZonedDateTime dateTimeFrom) throws ScreeningException;
}
