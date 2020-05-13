package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.model.exception.HospitalErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleValidation(MethodArgumentNotValidException exc) {
        List<ErrorModel> errors = exc.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorModel(fieldError, HospitalErrorCode.VALIDATION_ERROR))
                .collect(Collectors.toList());
        return new MyError(errors);
    }

    public static class MyError {
        private List<ErrorModel> allErrors = new ArrayList<>();

        public MyError() {
        }

        public MyError(List<ErrorModel> allErrors) {
            this.allErrors = allErrors;
        }

        public List<ErrorModel> getAllErrors() {
            return allErrors;
        }

        public void setAllErrors(List<ErrorModel> allErrors) {
            this.allErrors = allErrors;
        }
    }
}
