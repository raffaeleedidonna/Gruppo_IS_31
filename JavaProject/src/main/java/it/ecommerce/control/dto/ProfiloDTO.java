package it.ecommerce.control.dto;

public record ProfiloDTO(
        String nome,
        String cognome,
        String email,
        String indirizzoSpedizionePrincipale,
        String immagineProfilo) {
}
