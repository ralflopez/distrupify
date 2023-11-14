package com.distrupify.auth.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordService {
    private static final int WORK_FACTOR = 15;

    public String hash(String rawPassword) {
        return BCrypt.withDefaults().hashToString(WORK_FACTOR, rawPassword.toCharArray());
    }

    public boolean isEqual(String rawPassword, String passwordHash) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), passwordHash);
        return result.verified;
    }
}
