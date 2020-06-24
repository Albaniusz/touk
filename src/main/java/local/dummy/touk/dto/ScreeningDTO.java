package local.dummy.touk.dto;

import local.dummy.touk.entity.Screening;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScreeningDTO extends RepresentationModel<ScreeningDTO> {
	private Long id;

	private ZonedDateTime screeningTime;

	private RoomDTO room;

	public ScreeningDTO(Screening screening) {
		id = screening.getId();
		screeningTime = screening.getScreeningTime();
	}
}
