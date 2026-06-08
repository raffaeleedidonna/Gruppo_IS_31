package it.ecommerce.control;

import it.ecommerce.entity.persistenza.AzioneTransazionale;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;

final class TransazioniDirette implements GestoreTransazioni {

    @Override
    public UnitaDiLavoro apreUnitaDiLavoro() {
        return null;
    }

    @Override
    public <R> R inTransazione(AzioneTransazionale<R> azione) {
        return azione.esegui();
    }
}
