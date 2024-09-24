package ewm.error;

import ewm.error.exception.ExistException;
import ewm.error.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ErrorHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ErrorResponse handleDateTimeParseException(DateTimeParseException e) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST,
				"Ошибка Валидации",
				e.getMessage());
	}


	@ExceptionHandler({ExistException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleParameterConflict(final Exception e) {
		return new ErrorResponse(HttpStatus.CONFLICT,
				"Ошибка уникальности",
				e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(final Exception e) {
		return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				"Произошла непредвиденная ошибка.",
				e.getMessage()
		);
	}
}