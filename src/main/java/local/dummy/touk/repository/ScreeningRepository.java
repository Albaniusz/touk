package local.dummy.touk.repository;

import local.dummy.touk.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
	/**
	 * Get all on-occupied seats for specific screening
	 *
	 * @param screeningId id of screening
	 * @return Optional of Screening
	 * @throws Exception
	 */
	@Query("SELECT s FROM Screening s JOIN FETCH s.room r JOIN FETCH r.rows ro JOIN FETCH ro.seats se" +
			" LEFT OUTER JOIN se.tickets t WHERE s.id = :screeningId AND t.screening IS NULL")
	Optional<Screening> getScreeningByQueryWithId(@Param("screeningId") Long screeningId) throws Exception;
}
