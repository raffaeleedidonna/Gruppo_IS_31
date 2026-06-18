package it.ecommerce.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import it.ecommerce.entity.persistenza.UnitaDiLavoro;

final class UnitaDiLavoroHibernate implements UnitaDiLavoro {

    private final SessionFactory sessionFactory;
    private Session sessione;
    private Transaction transazione;

    UnitaDiLavoroHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void inizia() {
        sessione = sessionFactory.getCurrentSession();
        transazione = sessione.beginTransaction();
    }

    @Override
    public void conferma() {
        if (transazione != null && transazione.isActive()) {
            transazione.commit();
        }
    }

    @Override
    public void annulla() {
        if (transazione != null && transazione.isActive()) {
            transazione.rollback();
        }
    }

    @Override
    public void chiudi() {
        if (sessione != null && sessione.isOpen()) {
            sessione.close();
        }
    }
}
