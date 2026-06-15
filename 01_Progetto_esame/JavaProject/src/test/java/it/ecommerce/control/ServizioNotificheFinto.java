package it.ecommerce.control;

import java.util.ArrayList;
import java.util.List;

import it.ecommerce.entity.servizi.MessaggioNotifica;
import it.ecommerce.entity.servizi.ServizioNotifiche;

final class ServizioNotificheFinto implements ServizioNotifiche {

    private final List<MessaggioNotifica> inviati = new ArrayList<>();

    List<MessaggioNotifica> inviati() {
        return inviati;
    }

    MessaggioNotifica ultimo() {
        return inviati.isEmpty() ? null : inviati.get(inviati.size() - 1);
    }

    @Override
    public void invia(MessaggioNotifica messaggio) {
        inviati.add(messaggio);
    }
}
