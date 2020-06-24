package local.dummy.touk.controller;

import local.dummy.touk.dto.ReservationDTO;
import local.dummy.touk.exception.ReservationException;
import local.dummy.touk.form.MakeReservationForm;
import local.dummy.touk.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class ReservationController {
	private final static Logger logger = LoggerFactory.getLogger(ReservationController.class);

	ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping("/reservation")
	public ResponseEntity reservation(@ModelAttribute("reservation") @Valid MakeReservationForm reservationForm,
									  BindingResult result) throws Exception {
		logger.debug("Action: reservation with data {}", reservationForm);

		if (result.hasErrors()) {
			throw new ReservationException("Incorrect reservation data");
		}

		ReservationDTO response = reservationService.makeReservation(reservationForm);

		logger.debug("Action: done");
		return ResponseEntity.created(new URI("/reservation/123")).body(response);
	}
}
