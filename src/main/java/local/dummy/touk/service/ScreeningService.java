package local.dummy.touk.service;

import local.dummy.touk.entity.Screening;

public interface ScreeningService {
	Screening getScreeningById(Long screeningId) throws Exception;
}
