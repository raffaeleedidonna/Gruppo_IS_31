package it.ecommerce.database;

import org.hibernate.SessionFactory;

import it.ecommerce.entity.persistenza.CatalogoRepository;
import it.ecommerce.entity.persistenza.CategoriaRepository;
import it.ecommerce.entity.persistenza.FornitorePersistenza;
import it.ecommerce.entity.persistenza.GestoreTransazioni;
import it.ecommerce.entity.persistenza.OrdineRepository;
import it.ecommerce.entity.persistenza.UtenteRepository;
import it.ecommerce.entity.servizi.ServizioNotifiche;

public final class FornitorePersistenzaHibernate implements FornitorePersistenza {

    private final GestoreTransazioni gestoreTransazioni;
    private final CatalogoRepository catalogoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UtenteRepository utenteRepository;
    private final OrdineRepository ordineRepository;
    private final ServizioNotifiche servizioNotifiche;

    public FornitorePersistenzaHibernate() {
        SessionFactory sessionFactory = SessionFactoryProvider.sessionFactory();
        this.gestoreTransazioni = new GestoreTransazioniHibernate(sessionFactory);
        this.catalogoRepository = new CatalogoRepositoryHibernate(sessionFactory);
        this.categoriaRepository = new CategoriaRepositoryHibernate(sessionFactory);
        this.utenteRepository = new UtenteRepositoryHibernate(sessionFactory);
        this.ordineRepository = new OrdineRepositoryHibernate(sessionFactory);
        this.servizioNotifiche = new ServizioNotificheLog();
        SeedSviluppo.popolaSeNecessario(this);
    }

    @Override
    public GestoreTransazioni gestoreTransazioni() {
        return gestoreTransazioni;
    }

    @Override
    public CatalogoRepository catalogoRepository() {
        return catalogoRepository;
    }

    @Override
    public CategoriaRepository categoriaRepository() {
        return categoriaRepository;
    }

    @Override
    public UtenteRepository utenteRepository() {
        return utenteRepository;
    }

    @Override
    public OrdineRepository ordineRepository() {
        return ordineRepository;
    }

    @Override
    public ServizioNotifiche servizioNotifiche() {
        return servizioNotifiche;
    }
}
