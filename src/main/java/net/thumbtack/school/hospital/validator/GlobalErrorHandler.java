package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.service.AdminService;
import net.thumbtack.school.hospital.validator.exception.HospitalErrorCode;
import net.thumbtack.school.hospital.validator.exception.HospitalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleValidation(MethodArgumentNotValidException exc) {
        List<ErrorModel> errors = exc.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> {
                    ErrorModel er = new ErrorModel(fieldError, HospitalErrorCode.VALIDATION_ERROR);
                    LOGGER.debug("GlobalErrorHandler validation error {}, field {}, massage {} ",
                            er.getErrorCode(), er.getField(), er.getMassage());
                    return er;
                })
                .collect(Collectors.toList());
        return new MyError(errors);
    }

    @ExceptionHandler(HospitalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleHospitalException(HospitalException exc) {
        List<ErrorModel> errors = new ArrayList<>();
        errors.add(exc.getErrorModel());
        LOGGER.debug("GlobalErrorHandler validation error {}, field {}, massage {} ",
                exc.getErrorModel().getErrorCode(), exc.getErrorModel().getField(), exc.getErrorModel().getMassage());
        return new MyError(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        List<ErrorModel> errors = new ArrayList<>();
        String error =
                ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();

        ErrorModel er = new ErrorModel(HospitalErrorCode.ARGUMENT_MISMATCH_ERROR, ex.getName(), error);
        errors.add(er);
        return new MyError(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex) {
        List<ErrorModel> errors = new ArrayList<>();
        String error = ex.getParameterName() + " parameter is missing";

        ErrorModel er = new ErrorModel(HospitalErrorCode.ARGUMENT_MISMATCH_ERROR, ex.getParameterName(), error);
        errors.add(er);
        return new MyError(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleConstraintViolation(
            ConstraintViolationException ex) {
        List<ErrorModel> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorModel(HospitalErrorCode.CONSTRAINT_VIOLATION_ERROR,
                    violation.getRootBeanClass().getName(),
                    violation.getRootBeanClass().getName() + " " +
                            violation.getPropertyPath() + ": " + violation.getMessage()));
        }
        return new MyError(errors);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public MyError handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        List<ErrorModel> errors = new ArrayList<>();
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ErrorModel er = new ErrorModel(HospitalErrorCode.ARGUMENT_MISMATCH_ERROR, ex.getRequestURL(), error);
        errors.add(er);
        return new MyError(errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public MyError handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        List<ErrorModel> errors = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));
        ErrorModel er = new ErrorModel(HospitalErrorCode.METHOD_NOT_ALLOWED, ex.getMethod(), builder.toString());
        errors.add(er);
        return new MyError(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        List<ErrorModel> errors = new ArrayList<>();
        ErrorModel er = new ErrorModel(HospitalErrorCode.HTTP_MESSAGE_NOT_READABLE, "Malformed JSON request",
                ex.getLocalizedMessage());
        errors.add(er);
        return new MyError(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyError handleAll(Exception ex) {
        List<ErrorModel> errors = new ArrayList<>();
        ErrorModel er = new ErrorModel(HospitalErrorCode.INTERNAL_SERVER_ERROR, "error occurred",
                ex.getLocalizedMessage());
        errors.add(er);
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
