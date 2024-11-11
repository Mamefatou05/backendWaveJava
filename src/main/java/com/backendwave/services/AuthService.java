package com.backendwave.services;

import com.backendwave.web.dto.request.users.LoginDep;
import org.springframework.security.core.Authentication;

public interface AuthService {

    Authentication authenticateUser(LoginDep loginDepRequest);
}
