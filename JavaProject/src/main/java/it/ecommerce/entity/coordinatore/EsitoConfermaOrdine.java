package it.ecommerce.entity.coordinatore;

import it.ecommerce.entity.Ordine;

public record EsitoConfermaOrdine(boolean confermato, String messaggio, Ordine ordine) {
}
