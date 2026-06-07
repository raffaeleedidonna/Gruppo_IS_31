package it.ecommerce.control.dto;

import java.time.LocalDateTime;

public record NotificaDTO(Long id, String messaggio, LocalDateTime dataCreazione) {
}
