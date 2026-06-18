package it.ecommerce.database;

import org.hibernate.SessionFactory;

import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.UnitaDiLavoro;

final class GestoreTransazioniHibernate implements GestoreTransazioni {

    private final SessionFactory sessionFactory;

    GestoreTransazioniHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UnitaDiLavoro apreUnitaDiLavoro() {
        return new UnitaDiLavoroHibernate(sessionFactory);
    }
}
