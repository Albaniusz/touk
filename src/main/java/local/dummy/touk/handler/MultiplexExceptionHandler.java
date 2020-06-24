package local.dummy.touk.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import local.dummy.touk.exception.ReservationException;
import local.dummy.touk.exception.ScreeningException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@ControllerAdvice
@SuppressWarnings("unused")
@ResponseBody
public class MultiplexExceptionHandler {
	private static ObjectMapper mapper = new ObjectMapper();

	public static class ErrorResponse {
		private static final MultiValueMap<String, String> HEADERS = new LinkedMultiValueMap<>(
				Collections.singletonMap(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE))
		);
		private final Exception exception;
		private HttpStatus responseStatus;

		ErrorResponse(HttpStatus responseStatus, Exception exception) {
			this.exception = exception;
			this.responseStatus = responseStatus;
		}

		ResponseEntity<String> buildResponse() {
			try {
				return new ResponseEntity<>(mapper.writeValueAsString(this), HEADERS, responseStatus);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

		public static MultiValueMap<String, String> getHEADERS() {
			return HEADERS;
		}

		public Exception getException() {
			return exception;
		}

		public HttpStatus getResponseStatus() {
			return responseStatus;
		}
	}

	@ExceptionHandler(ScreeningException.class)
	public ResponseEntity<String> handleNotFound(ScreeningException exception) {
		return new ErrorResponse(HttpStatus.NOT_FOUND, exception).buildResponse();
	}

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<String> handleNumberFormat(ReservationException exception) {
		return new ErrorResponse(HttpStatus.NOT_FOUND, exception).buildResponse();
	}
}
