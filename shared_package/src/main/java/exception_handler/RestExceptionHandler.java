package exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> illegalExceptionHandler(IllegalArgumentException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Error", exception.getMessage());
        map.put("Error Code", HttpStatus.EXPECTATION_FAILED.toString());
        return map;
    };
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(Exception.class)
    public Map<String, String> exceptionHandler(Exception exception) {
        Map<String, String> map = new HashMap<>();
        map.put("Error", exception.getMessage());
        return map;
    };
}
