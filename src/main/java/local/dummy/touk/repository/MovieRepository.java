package local.dummy.touk.repository;

import local.dummy.touk.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	/**
	 * Get movies with screenings, where screening is between dateTimeFrom and dateTimeTo
	 *
	 * @param dateTimeFrom dateTime from
	 * @param dateTimeTo   dateTime to
	 * @return list of movies
	 */
	@Query("SELECT DISTINCT m, s FROM Movie m JOIN FETCH m.screenings s WHERE s.screeningTime" +
			" BETWEEN :dateTimeFrom AND :dateTimeTo ORDER BY m.title, s.screeningTime ASC")
	List<Movie> getMoviesByQueryWithDate(@Param("dateTimeFrom") ZonedDateTime dateTimeFrom,
										 @Param("dateTimeTo") ZonedDateTime dateTimeTo);
}
