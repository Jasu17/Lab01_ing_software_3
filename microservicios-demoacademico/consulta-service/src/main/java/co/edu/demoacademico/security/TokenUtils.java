package co.edu.demoacademico.security;

public final class TokenUtils {

    private TokenUtils() {
    }

    public static String normalize(String rawToken) {
        if (rawToken == null) {
            return null;
        }

        String token = rawToken.trim();

        while (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7).trim();
        }

        return token.isBlank() ? null : token;
    }
}
