package it.ecommerce.database;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.persistenza.OrdineRepository;

final class OrdineRepositoryHibernate implements OrdineRepository {

    private final SessionFactory sessionFactory;

    OrdineRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Ordine salva(Ordine ordine) {
        if (ordine.getId() == null) {
            sessione().persist(ordine);
            return ordine;
        }
        return sessione().merge(ordine);
    }

    @Override
    public Optional<Ordine> perId(Long id) {
        return Optional.ofNullable(sessione().find(Ordine.class, id));
    }

    @Override
    public List<Ordine> tutti() {
        return sessione().createQuery("from Ordine o order by o.dataCreazione desc", Ordine.class)
                .getResultList();
    }

    @Override
    public List<Ordine> perCliente(Long clienteId) {
        return sessione().createQuery(
                "from Ordine o where o.cliente.id = :clienteId order by o.dataCreazione desc",
                Ordine.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }
}
