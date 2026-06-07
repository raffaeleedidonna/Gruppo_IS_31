package it.ecommerce.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import it.ecommerce.entity.Amministratore;
import it.ecommerce.entity.Carrello;
import it.ecommerce.entity.Categoria;
import it.ecommerce.entity.Cliente;
import it.ecommerce.entity.Notifica;
import it.ecommerce.entity.Ordine;
import it.ecommerce.entity.Prodotto;
import it.ecommerce.entity.Profilo;
import it.ecommerce.entity.RigaCarrello;
import it.ecommerce.entity.RigaOrdine;
import it.ecommerce.entity.Utente;

final class SessionFactoryProvider {

    private static SessionFactory sessionFactory;

    private SessionFactoryProvider() {
    }

    static synchronized SessionFactory sessionFactory() {
        if (sessionFactory == null) {
            Configuration configurazione = new Configuration().configure();
            configurazione.addAnnotatedClass(Categoria.class);
            configurazione.addAnnotatedClass(Prodotto.class);
            configurazione.addAnnotatedClass(Profilo.class);
            configurazione.addAnnotatedClass(Utente.class);
            configurazione.addAnnotatedClass(Amministratore.class);
            configurazione.addAnnotatedClass(Cliente.class);
            configurazione.addAnnotatedClass(Carrello.class);
            configurazione.addAnnotatedClass(RigaCarrello.class);
            configurazione.addAnnotatedClass(Ordine.class);
            configurazione.addAnnotatedClass(RigaOrdine.class);
            configurazione.addAnnotatedClass(Notifica.class);
            sessionFactory = configurazione.buildSessionFactory();
        }
        return sessionFactory;
    }
}
