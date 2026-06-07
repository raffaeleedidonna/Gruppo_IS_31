package it.ecommerce.database;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Utente;
import it.ecommerce.entity.persistenza.UtenteDAO;

final class UtenteDAOHibernate implements UtenteDAO {

    private final SessionFactory sessionFactory;

    UtenteDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Utente salva(Utente utente) {
        if (utente.getId() == null) {
            sessione().persist(utente);
            return utente;
        }
        return sessione().merge(utente);
    }

    @Override
    public Optional<Utente> perEmail(String email) {
        return sessione().createQuery("from Utente u where u.email = :email", Utente.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Utente> perId(Long id) {
        return Optional.ofNullable(sessione().find(Utente.class, id));
    }

    @Override
    public boolean esisteEmail(String email) {
        Long conteggio = sessione().createQuery(
                "select count(u) from Utente u where u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return conteggio > 0;
    }

    @Override
    public long contaClienti() {
        return sessione().createQuery("select count(c) from Cliente c", Long.class)
                .getSingleResult();
    }
}
