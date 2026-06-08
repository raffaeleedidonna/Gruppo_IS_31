package it.ecommerce.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrdineTest {

    private Ordine ordineVuoto() {
        return new Ordine(new Cliente("c@x.it", "pw", new Profilo("M", "R", null, null)), "Via Test 1");
    }

    private Prodotto prodotto() {
        return new Prodotto("Penna", "", new Categoria("Cancelleria"));
    }

    @Test
    void ordineSenzaRigheNonRispettaInvariante() {
        Ordine ordine = ordineVuoto();
        assertThrows(EccezioneValidazione.class, ordine::assicuraNonVuoto);
    }

    @Test
    void ordineConAlmenoUnaRigaRispettaInvariante() {
        Ordine ordine = ordineVuoto();
        ordine.aggiungiRiga(prodotto(), 1, new BigDecimal("2.00"));
        assertDoesNotThrow(ordine::assicuraNonVuoto);
    }
}
