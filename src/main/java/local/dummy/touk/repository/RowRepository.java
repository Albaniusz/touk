package local.dummy.touk.repository;

import local.dummy.touk.entity.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RowRepository extends JpaRepository<Row, Long> {
	/**
	 * Find all seats in one row in screening
	 *
	 * @param screeningId
	 * @param rowId
	 * @return
	 */
	@Query("SELECT ro FROM Row ro" +
			" JOIN FETCH ro.seats se" +
			" INNER JOIN ro.room r" +
			" INNER JOIN r.screenings s" +
			" WHERE s.id = :screeningId" +
			" AND ro.id = :rowId")
	Optional<Row> findRowInScreeningWithSeats(@Param("screeningId") Long screeningId,
											  @Param("rowId") Long rowId);
}
