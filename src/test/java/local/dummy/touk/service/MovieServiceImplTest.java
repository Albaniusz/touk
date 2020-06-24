package local.dummy.touk.service;

import local.dummy.touk.entity.Movie;
import local.dummy.touk.entity.Screening;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService = new MovieServiceImpl(movieRepository);

    @Test
    public void shouldFindSearchDateIsPast() {
        // given
        ZonedDateTime dateTimeFrom = ZonedDateTime.now().minusMinutes(10);

        // when
        // then
        assertThrows(ScreeningException.class, () -> {
            movieService.getMoviesByDateTime(dateTimeFrom);
        });
    }

    @Test
    public void shouldFindSearchDateIsMoreThan14DaysInTheFuture() {
        // given
        ZonedDateTime dateTimeFrom = ZonedDateTime.now().plusDays(14);

        // when
        // then
        assertThrows(ScreeningException.class, () -> {
            movieService.getMoviesByDateTime(dateTimeFrom);
        });
    }

    @Test
    public void shouldFindEmptyListOfMovies() {
        // given
        ZonedDateTime dateTimeFrom = ZonedDateTime.now().plusMinutes(15);

        // when
        // then
        assertThrows(ScreeningException.class, () -> {
            movieService.getMoviesByDateTime(dateTimeFrom);
        });
    }

    @Test
    public void shouldFindFilledListOfMovies() throws ScreeningException {
        // given
        ZonedDateTime dateTimeFrom = ZonedDateTime.now().plusMinutes(15);

        Movie movie = Mockito.mock(Movie.class);
        movie.setTitle("Lorem ipsum");
        movie.setLength((short) 123);
        movie.setDatePremiere(LocalDate.parse("1990-01-01"));
        movie.setOnOfferFrom(LocalDate.parse("2020-02-01"));
        movie.setOnOfferTo(LocalDate.parse("2050-12-31"));

        Screening screening = Mockito.mock(Screening.class);
        screening.setId(1L);
        screening.setScreeningTime(ZonedDateTime.now().plusHours(1));
        screening.setMovie(movie);

        movie.addScreening(screening);

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        // when
        when(movieRepository.getMoviesByQueryWithDate(dateTimeFrom, dateTimeFrom.plusHours(8)))
                .thenReturn(movies);

        // then
        assertEquals(1, movieService.getMoviesByDateTime(dateTimeFrom).size());
    }
}
