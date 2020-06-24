package local.dummy.touk.dto;

import local.dummy.touk.entity.Seat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class SeatDTO extends RepresentationModel<SeatDTO> {
	private Long id;

	private Short seatNumber;

	public SeatDTO(Seat seat) {
		id = seat.getId();
		seatNumber = seat.getSeatNumber();
	}
}
