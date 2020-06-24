package local.dummy.touk.form;

import local.dummy.touk.validator.IsCapitalName;
import local.dummy.touk.validator.IsCapitalSurname;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MakeReservationForm {
	@NotEmpty
	@Size(min = 3)
	@IsCapitalName
	private String name;

	@NotEmpty
	@Size(min = 3)
	@IsCapitalSurname
	private String surname;

	@NotNull
	private Long screeningId;

	@NotNull
	private Long kindOfTicketId;

	@NotNull
	@Size(min = 1)
	private List<Long> seatsIds = new ArrayList<>();
}
