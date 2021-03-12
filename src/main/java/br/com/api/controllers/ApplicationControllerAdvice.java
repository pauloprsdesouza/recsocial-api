package br.com.api.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.api.authorization.HttpContext;
import br.com.api.infrastructure.database.datamodel.logerrors.LogError;
import br.com.api.infrastructure.database.datamodel.logerrors.LogErrorRepository;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @Autowired
    private LogErrorRepository _errors;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handlerMethodNotValidException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(p -> p.getDefaultMessage()).collect(Collectors.toList());

        return ResponseEntity.unprocessableEntity().body(errors);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> unknownException(Exception ex, HttpServletRequest req) {
        UserAccount user = HttpContext.getUserLogged();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        LogError error = new LogError();
        error.setActiveUser(user);
        error.setStackTrace(stackTrace);
        error.setUrl(req.getRequestURL().toString());
        error.setRegistrationDate(new Date());

        _errors.save(error);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
