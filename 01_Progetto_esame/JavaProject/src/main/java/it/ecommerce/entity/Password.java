package it.ecommerce.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

final class Password {

    private Password() {
    }

    static String hash(String testoInChiaro) {
        try {
            MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
            byte[] digest = algoritmo.digest(testoInChiaro.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException eccezione) {
            throw new IllegalStateException(eccezione);
        }
    }
}
