package com.lms.frontend.util;


public class ConstantUtil {
//
    public static final String HOST_URL = System.getenv("BACKEND_URL") != null
            ? System.getenv("BACKEND_URL")
            : "http://localhost:8888";

    public static final String[] ENDPOINT_WHITELIST = {
            "/css/**",
            "/img/**",
            "/js/**",
            "/lib/**",
            "/scss/**",
            "/",
            "/home",
            "/login"
    };

}

