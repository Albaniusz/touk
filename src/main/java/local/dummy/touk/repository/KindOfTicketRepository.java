package local.dummy.touk.repository;

import local.dummy.touk.entity.KindOfTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindOfTicketRepository extends JpaRepository<KindOfTicket, Long> {
}
