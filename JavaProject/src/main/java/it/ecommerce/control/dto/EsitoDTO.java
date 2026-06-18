package it.ecommerce.control.dto;

public record EsitoDTO<T>(boolean successo, String messaggio, T dato) {

    public static <T> EsitoDTO<T> successo(String messaggio, T dato) {
        return new EsitoDTO<>(true, messaggio, dato);
    }

    public static <T> EsitoDTO<T> errore(String messaggio) {
        return new EsitoDTO<>(false, messaggio, null);
    }
}
