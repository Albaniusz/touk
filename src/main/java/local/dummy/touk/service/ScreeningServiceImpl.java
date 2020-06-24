package local.dummy.touk.service;

import local.dummy.touk.entity.Screening;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.repository.ScreeningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ScreeningServiceImpl implements ScreeningService {
	private final static Logger logger = LoggerFactory.getLogger(ScreeningServiceImpl.class);

	private ScreeningRepository screeningRepository;

	public ScreeningServiceImpl(ScreeningRepository screeningRepository) {
		this.screeningRepository = screeningRepository;
	}

	@Override
	@Transactional
	public Screening getScreeningById(Long screeningId) throws Exception {
		return screeningRepository.getScreeningByQueryWithId(screeningId).orElseThrow(() -> {
			logger.error("No screening with ID {}", screeningId);
			return new ScreeningException("No screening with ID " + screeningId);
		});
	}
}
