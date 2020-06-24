package local.dummy.touk.service;

import local.dummy.touk.entity.Screening;
import local.dummy.touk.exception.ScreeningException;
import local.dummy.touk.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceImplTest {
    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private final ScreeningService screeningService = new ScreeningServiceImpl(screeningRepository);

    @Test
    public void shouldFindScreeningById() throws Exception {
        // given
        long screeningId = 1;

        Screening screening = Mockito.mock(Screening.class);
        screening.setId(screeningId);

        // when
        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.of(screening));

        // then
        assertDoesNotThrow(() -> {
            screeningService.getScreeningById(screeningId);
        });
    }

    @Test
    public void shouldNotFindScreeningById() throws Exception {
        // given
        long screeningId = -1;

        // when
        when(screeningRepository.getScreeningByQueryWithId(screeningId))
                .thenReturn(Optional.empty());

        // then
        assertThrows(ScreeningException.class, () -> {
            screeningService.getScreeningById(screeningId);
        });
    }
}
