package it.ecommerce.control.dto;

public record RegistrazioneDTO(
        String nome,
        String cognome,
        String email,
        String password,
        String indirizzoSpedizione,
        String immagineProfilo) {
}
