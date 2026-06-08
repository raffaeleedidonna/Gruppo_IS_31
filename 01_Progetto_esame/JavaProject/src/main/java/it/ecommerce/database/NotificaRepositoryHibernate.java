package it.ecommerce.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Notifica;
import it.ecommerce.entity.persistenza.NotificaRepository;

final class NotificaRepositoryHibernate implements NotificaRepository {

    private final SessionFactory sessionFactory;

    NotificaRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Notifica salva(Notifica notifica) {
        sessione().persist(notifica);
        return notifica;
    }

    @Override
    public List<Notifica> perCliente(Long clienteId) {
        return sessione().createQuery(
                "from Notifica n where n.cliente.id = :clienteId order by n.dataCreazione desc",
                Notifica.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }
}
