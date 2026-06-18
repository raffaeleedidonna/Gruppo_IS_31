package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import it.ecommerce.control.GestioneProdottiController;
import it.ecommerce.control.dto.CategoriaDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;

public class GestioneProdottiView extends JPanel {

    private final GestioneProdottiController controller;
    private final ModelloTabellaProdotti modello = new ModelloTabellaProdotti();
    private final JTable tabella = new JTable(modello);

    public GestioneProdottiView(GestioneProdottiController controller) {
        super(new BorderLayout());
        this.controller = controller;
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(creaBarraStrumenti(), BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        ricarica();
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        JButton aggiungi = new JButton("Aggiungi");
        JButton modifica = new JButton("Modifica");
        JButton rimuovi = new JButton("Rimuovi dal catalogo");
        JButton aggiorna = new JButton("Aggiorna");
        aggiungi.addActionListener(evento -> apriAggiunta());
        modifica.addActionListener(evento -> apriModifica());
        rimuovi.addActionListener(evento -> rimuoviSelezionato());
        aggiorna.addActionListener(evento -> ricarica());
        barra.add(aggiungi);
        barra.add(modifica);
        barra.add(rimuovi);
        barra.add(aggiorna);
        return barra;
    }

    private void ricarica() {
        modello.impostaProdotti(controller.elencoProdotti());
    }

    private void apriAggiunta() {
        List<CategoriaDTO> categorie = controller.categorie();
        if (categorie.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessuna categoria disponibile.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new ProdottoDialog(SwingUtilities.getWindowAncestor(this), categorie, null).mostra().ifPresent(prodotto -> {
            EsitoDTO<ProdottoDTO> esito = controller.aggiungiProdotto(prodotto);
            mostraEsito(esito.successo(), esito.messaggio());
            if (esito.successo()) {
                ricarica();
            }
        });
    }

    private void apriModifica() {
        ProdottoDTO selezionato = prodottoSelezionato();
        if (selezionato == null) {
            return;
        }
        ProdottoDTO corrente = controller.dettaglioProdotto(selezionato.id());
        if (corrente == null) {
            JOptionPane.showMessageDialog(this, "Prodotto non più disponibile.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            ricarica();
            return;
        }
        new ProdottoDialog(SwingUtilities.getWindowAncestor(this), controller.categorie(), corrente)
                .mostra().ifPresent(prodotto -> {
                    EsitoDTO<ProdottoDTO> esito = controller.modificaProdotto(prodotto);
                    mostraEsito(esito.successo(), esito.messaggio());
                    if (esito.successo()) {
                        ricarica();
                    }
                });
    }

    private void rimuoviSelezionato() {
        ProdottoDTO selezionato = prodottoSelezionato();
        if (selezionato == null) {
            return;
        }
        int conferma = JOptionPane.showConfirmDialog(this,
                "Rimuovere \"" + selezionato.nome() + "\" dal catalogo?",
                "Conferma rimozione", JOptionPane.YES_NO_OPTION);
        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }
        EsitoDTO<Void> esito = controller.rimuoviDalCatalogo(selezionato.id());
        mostraEsito(esito.successo(), esito.messaggio());
        if (esito.successo()) {
            ricarica();
        }
    }

    private ProdottoDTO prodottoSelezionato() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un prodotto.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return modello.prodottoA(tabella.convertRowIndexToModel(riga));
    }

    private void mostraEsito(boolean successo, String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio,
                successo ? "Operazione riuscita" : "Errore",
                successo ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}
