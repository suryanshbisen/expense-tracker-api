package com.ssb.expense_tracker_api.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInMs,
        String username,
        String email
) {
    public static AuthResponse of(String token,long expiresInMs,String username, String email){
        return new AuthResponse(token,"Bearer", expiresInMs, username, email);
    }
}
