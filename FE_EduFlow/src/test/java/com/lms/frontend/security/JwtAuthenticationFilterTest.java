package com.lms.frontend.security;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter();
    }

    @Test
    void missingSessionRedirectsToSignIn() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/student/edit");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals("/signin", response.getRedirectedUrl());
    }

    @Test
    void missingAccessTokenInvalidatesSessionAndRedirectsToSignIn() throws Exception {
        MockHttpServletRequest request = authenticatedRequest("/student/edit", "STUDENT", null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals("/signin", response.getRedirectedUrl());
    }

    @Test
    void missingRoleReturns403InsteadOfThrowingNullPointerException() throws Exception {
        MockHttpServletRequest request = authenticatedRequest("/student/edit", null);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    void crossRoleAccessIsForbiddenForEveryProtectedArea() throws Exception {
        assertForbidden("/student/edit", "ADMIN");
        assertForbidden("/admin", "STUDENT");
        assertForbidden("/instructor/profile", "STUDENT");
        assertForbidden("/course/learn", "INSTRUCTOR");
        assertForbidden("/course/checkout", "ADMIN");
    }

    @Test
    void matchingRoleAndRolePrefixAreAccepted() throws Exception {
        assertAllowed("/student/edit", "STUDENT");
        assertAllowed("/admin", "ROLE_ADMIN");
        assertAllowed("/instructor/profile", "instructor");
    }

    private void assertForbidden(String uri, String role) throws Exception {
        MockHttpServletRequest request = authenticatedRequest(uri, role);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    private void assertAllowed(String uri, String role) throws Exception {
        MockHttpServletRequest request = authenticatedRequest(uri, role);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertTrue(filter.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    private MockHttpServletRequest authenticatedRequest(String uri, String role) {
        return authenticatedRequest(uri, role, "token");
    }

    private MockHttpServletRequest authenticatedRequest(String uri, String role, String token) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        MockHttpSession session = new MockHttpSession();
        AuthResponse user = new AuthResponse();
        user.setAccessToken(token);
        user.setUsername("user@example.com");
        user.setRole(role);
        session.setAttribute("userLogin", user);
        request.setSession(session);
        return request;
    }
}
