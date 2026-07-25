package com.lms.backend.exception;

import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.util.ValidatorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiResponse handleForbiddenException(ForbiddenException ex) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.error(ex.getMessage());

        return apiResponse;
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
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error("The username or password is incorrect.");
        return apiResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error(ex.getMessage());
        return apiResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse handleUnexpectedException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.error("Internal server error");
        return apiResponse;
    }

}
