package com.lolsearcher.reactive.authentication;

public class AuthenticationConstant {

    public static final String JWT_RECREATION_URI = "http://localhost:8082/jwt";
    public static final String CERTIFICATION_SERVER_TOKEN_VALIDATION_URI = "http://localhost:8082/jwt?token={token}";

    public static final String CERTIFICATED_SERVER = "Server-Certification";
    public static final String CERTIFICATED_SERVER_USERID = "UserId";
    public static final String CERTIFICATED_SERVER_AUTHORITY = "Authority";
}
