package local.dummy.touk.dto;

import local.dummy.touk.entity.Row;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RowDTO extends RepresentationModel<RowDTO> {
	private Long id;

	private String name;

	private List<SeatDTO> seats;

	public RowDTO(Row row) {
		id = row.getId();
		name = row.getName();
	}
}
