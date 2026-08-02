package com.lms.frontend.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ErrorControllerTest {
    private ErrorController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("error.generic_desc", Locale.ENGLISH, "Temporarily unavailable");
        messageSource.addMessage("error.forbidden_desc", Locale.ENGLISH, "Access denied");
        controller = new ErrorController(messageSource);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void renders404PageForMissingRoute() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);

        String view = controller.showErrorPage(request, response, new ConcurrentModel(), Locale.ENGLISH);

        assertEquals("404", view);
        verify(response).setStatus(404);
    }

    @Test
    void rendersFriendlyPageForUnexpectedServerError() {
        ConcurrentModel model = new ConcurrentModel();
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        String view = controller.showErrorPage(request, response, model, Locale.ENGLISH);

        assertEquals("error", view);
        assertEquals(500, model.getAttribute("statusCode"));
        assertEquals("Temporarily unavailable", model.getAttribute("errorMessage"));
        verify(response).setStatus(500);
    }
}
