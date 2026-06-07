package it.ecommerce.database;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.persistenza.CategoriaDAO;

final class CategoriaDAOHibernate implements CategoriaDAO {

    private final SessionFactory sessionFactory;

    CategoriaDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session sessione() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Categoria salva(Categoria categoria) {
        if (categoria.getId() == null) {
            sessione().persist(categoria);
            return categoria;
        }
        return sessione().merge(categoria);
    }

    @Override
    public Optional<Categoria> perId(Long id) {
        return Optional.ofNullable(sessione().find(Categoria.class, id));
    }

    @Override
    public Optional<Categoria> perNome(String nome) {
        return sessione().createQuery(
                "from Categoria c where lower(c.nome) = :nome", Categoria.class)
                .setParameter("nome", nome.toLowerCase())
                .uniqueResultOptional();
    }

    @Override
    public List<Categoria> tutte() {
        return sessione().createQuery("from Categoria c order by c.nome", Categoria.class)
                .getResultList();
    }
}
