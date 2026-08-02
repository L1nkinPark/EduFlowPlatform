package com.lms.frontend.security;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter();
        jwtUtil = mock(JwtUtil.class);
        ReflectionTestUtils.setField(filter, "jwtUtil", jwtUtil);
    }

    @Test
    void missingSessionRedirectsToSignIn() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/student/edit");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals("/signin", response.getRedirectedUrl());
    }

    @Test
    void malformedTokenIsHandledAsAuthenticationFailureInsteadOf500() throws Exception {
        MockHttpServletRequest request = authenticatedRequest("/student/edit", "STUDENT");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = (MockHttpSession) request.getSession(false);
        when(jwtUtil.isTokenValid(anyString(), anyString())).thenThrow(new IllegalArgumentException("bad token"));

        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals("/signin", response.getRedirectedUrl());
        assertThrows(IllegalStateException.class, () -> session.getAttribute("userLogin"));
    }

    @Test
    void missingRoleReturns403InsteadOfThrowingNullPointerException() throws Exception {
        MockHttpServletRequest request = authenticatedRequest("/student/edit", null);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(jwtUtil.isTokenValid("token", "user@example.com")).thenReturn(true);

        assertFalse(filter.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    void crossRoleAccessIsForbiddenForEveryProtectedArea() throws Exception {
        when(jwtUtil.isTokenValid("token", "user@example.com")).thenReturn(true);

        assertForbidden("/student/edit", "ADMIN");
        assertForbidden("/admin", "STUDENT");
        assertForbidden("/instructor/profile", "STUDENT");
        assertForbidden("/course/learn", "INSTRUCTOR");
        assertForbidden("/course/checkout", "ADMIN");
    }

    @Test
    void matchingRoleAndRolePrefixAreAccepted() throws Exception {
        when(jwtUtil.isTokenValid("token", "user@example.com")).thenReturn(true);

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
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        MockHttpSession session = new MockHttpSession();
        AuthResponse user = new AuthResponse();
        user.setAccessToken("token");
        user.setUsername("user@example.com");
        user.setRole(role);
        session.setAttribute("userLogin", user);
        request.setSession(session);
        return request;
    }
}
