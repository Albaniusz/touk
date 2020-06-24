package local.dummy.touk.controller;

import local.dummy.touk.dto.RoomDTO;
import local.dummy.touk.dto.RowDTO;
import local.dummy.touk.dto.ScreeningDTO;
import local.dummy.touk.dto.SeatDTO;
import local.dummy.touk.entity.Screening;
import local.dummy.touk.service.ScreeningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ScreeningController {
	private final static Logger logger = LoggerFactory.getLogger(ScreeningController.class);

	private ScreeningService screeningService;

	public ScreeningController(ScreeningService screeningService) {
		this.screeningService = screeningService;
	}

	@GetMapping("/screening/{screeningId}")
	public ResponseEntity getByID(@PathVariable Long screeningId) throws Exception {
		logger.debug("Action: get screening with id: {}", screeningId);

		Screening screening = screeningService.getScreeningById(screeningId);

		ScreeningDTO screeningDTO = new ScreeningDTO(screening);
		screeningDTO.setRoom(new RoomDTO(screening.getRoom()));
		screeningDTO.getRoom().setRows(screening.getRoom().getRows().stream()
				.map(row -> {
					RowDTO rowDTO = new RowDTO(row);
					rowDTO.setSeats(row.getSeats().stream()
							.map(SeatDTO::new)
							.collect(Collectors.toList()));
					return rowDTO;
				})
				.collect(Collectors.toList())
		);

		screeningDTO.add(linkTo(methodOn(ScreeningController.class).getByID(screeningId)).withSelfRel());

		return new ResponseEntity<>(screeningDTO, HttpStatus.OK);
	}
}
