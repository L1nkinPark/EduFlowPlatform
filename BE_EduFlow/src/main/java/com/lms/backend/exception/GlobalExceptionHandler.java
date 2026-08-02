package com.lms.backend.exception;

import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.util.ValidatorUtil;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ApiResponse error(String message) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error(message);
        return apiResponse;
    }

    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleNotFound(Exception ex) {
        return error(ex.getMessage() == null ? "Resource not found" : ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse handleUnauthorized(UnauthorizedException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse handleConflict(ConflictException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(InvalidPromoCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleInvalidPromoCode(InvalidPromoCodeException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse handleInvalidToken(InvalidTokenException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ApiResponse handleMailException(MailException ex) {
        log.warn("Email service is unavailable: {}", ex.getMessage());
        return error("Email service is temporarily unavailable. Please try again later.");
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            BindException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleMalformedRequest(Exception ex) {
        return error("Invalid request");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse handleDataConflict(DataIntegrityViolationException ex) {
        log.info("Data conflict: {}", ex.getMostSpecificCause().getMessage());
        return error("The request conflicts with existing data.");
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiResponse handleForbiddenException(ForbiddenException ex) {
        return error(ex.getMessage());
    }

    // Ném ra bởi @PreAuthorize khi caller không có role phù hợp (ví dụ không phải ADMIN).
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiResponse handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.error("Access denied: admin privileges required.");

        return apiResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error("Validation failed",
                ValidatorUtil.toErrors(ex.getBindingResult().getFieldErrors()));
        return apiResponse;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex) {
        return error("The username or password is incorrect.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse handleUnexpectedException(Exception ex) {
        log.error("Unhandled server error", ex);
        return error("Internal server error");
    }

}
