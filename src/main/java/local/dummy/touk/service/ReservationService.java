package local.dummy.touk.service;

import local.dummy.touk.dto.ReservationDTO;
import local.dummy.touk.exception.ReservationException;
import local.dummy.touk.form.MakeReservationForm;

import java.util.List;

public interface ReservationService {
	ReservationDTO makeReservation(MakeReservationForm makeReservationForm) throws Exception;

	boolean checkIsNotTooLateToMakeReservation(long screeningId) throws Exception;

	boolean checkIsSeatBetweenSeatsNotOccupied(long screeningId, long rowId, List<Long> seatsIds) throws ReservationException;
}
