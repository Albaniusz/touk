package local.dummy.touk.repository;

import local.dummy.touk.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	/**
	 * Find seat in screening
	 *
	 * @param screeningId
	 * @param seatId
	 * @return
	 */
	@Query("SELECT se" +
			" FROM Seat se" +
			" INNER JOIN se.row ro" +
			" INNER JOIN ro.room r" +
			" INNER JOIN r.screenings s" +
			" LEFT JOIN se.tickets" +
			" WHERE se.id = :seatId AND s.id = :screeningId"
	)
	Optional<Seat> findSeatingInScreening(@Param("screeningId") Long screeningId,
										  @Param("seatId") Long seatId);
}
