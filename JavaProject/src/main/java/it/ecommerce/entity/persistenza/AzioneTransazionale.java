package it.ecommerce.entity.persistenza;

@FunctionalInterface
public interface AzioneTransazionale<R> {

    R esegui();
}
