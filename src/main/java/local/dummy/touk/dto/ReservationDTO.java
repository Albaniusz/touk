package local.dummy.touk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReservationDTO extends RepresentationModel<ReservationDTO> {
	private BigDecimal amount;

	private ZonedDateTime reservationExpirationDate;
}
