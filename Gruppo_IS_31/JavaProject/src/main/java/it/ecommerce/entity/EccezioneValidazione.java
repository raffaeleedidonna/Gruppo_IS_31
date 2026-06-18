package it.ecommerce.entity;

public class EccezioneValidazione extends RuntimeException {

    public EccezioneValidazione(String messaggio) {
        super(messaggio);
    }
}
