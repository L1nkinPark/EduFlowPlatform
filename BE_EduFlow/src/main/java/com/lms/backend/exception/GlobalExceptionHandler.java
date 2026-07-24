package com.lms.backend.exception;

import com.lms.backend.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.BadAttributeValueExpException;

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

//    @ExceptionHandler({Exception.class, RuntimeException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public ApiResponse handleException(Exception ex) {
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.error("Internal Server Error");
//
//        return apiResponse;
//    }

//    @ExceptionHandler(BadAttributeValueExpException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    public ApiResponse handleBadCredentialsException(BadAttributeValueExpException ex) {
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.error("Bad credentials");
//
//        return apiResponse;
//    }

//    @ExceptionHandler({InvalidTokenException.class, AccessDeniedException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ResponseBody
//    public ApiResponse handleInvalidTokenException(InvalidTokenException ex) {
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.error("Forbidden");
//
//        return apiResponse;
//    }

}
