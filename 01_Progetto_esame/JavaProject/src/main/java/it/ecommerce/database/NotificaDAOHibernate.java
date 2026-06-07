package it.ecommerce.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Notifica;
import it.ecommerce.entity.persistenza.NotificaDAO;

final class NotificaDAOHibernate implements NotificaDAO {

    private final SessionFactory sessionFactory;

    NotificaDAOHibernate(SessionFactory sessionFactory) {
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
