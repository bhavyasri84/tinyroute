package dev.tinyroute.common;
import dev.tinyroute.link.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class) ResponseEntity<ProblemDetail> notFound(RuntimeException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
    @ExceptionHandler(UrlGoneException.class) ResponseEntity<ProblemDetail> gone(RuntimeException e){return response(HttpStatus.GONE,e.getMessage());}
    @ExceptionHandler(AliasConflictException.class) ResponseEntity<ProblemDetail> conflict(RuntimeException e){return response(HttpStatus.CONFLICT,e.getMessage());}
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<ProblemDetail> badRequest(RuntimeException e){return response(HttpStatus.BAD_REQUEST,e.getMessage());}
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ProblemDetail> invalid(MethodArgumentNotValidException e){return response(HttpStatus.BAD_REQUEST,"Request validation failed");}
    private ResponseEntity<ProblemDetail> response(HttpStatus status,String message){return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status,message));}
}
