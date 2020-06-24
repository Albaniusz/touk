package local.dummy.touk.dto;

import local.dummy.touk.entity.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoomDTO extends RepresentationModel<RoomDTO> {
	private Long id;

	private String name;

	private List<RowDTO> rows;

	public RoomDTO(Room room) {
		id = room.getId();
		name = room.getName();
	}
}
