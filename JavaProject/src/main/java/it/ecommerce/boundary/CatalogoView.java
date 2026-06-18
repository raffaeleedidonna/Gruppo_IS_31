package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import it.ecommerce.control.CarrelloController;
import it.ecommerce.control.CatalogoController;
import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.ProdottoDTO;

public class CatalogoView extends JPanel {

    private final CatalogoController catalogo;
    private final CarrelloController carrello;
    private final Long clienteId;
    private final Runnable dopoAggiunta;
    private final ModelloTabellaProdotti modello = new ModelloTabellaProdotti();
    private final JTable tabella = new JTable(modello);
    private final JTextField campoRicerca = new JTextField(16);
    private final JTextArea dettaglioDescrizione = new JTextArea(3, 0);

    public CatalogoView(CatalogoController catalogo, CarrelloController carrello,
                        Long clienteId, Runnable dopoAggiunta) {
        super(new BorderLayout());
        this.catalogo = catalogo;
        this.carrello = carrello;
        this.clienteId = clienteId;
        this.dopoAggiunta = dopoAggiunta;
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.getSelectionModel().addListSelectionListener(evento -> aggiornaDescrizione());
        add(creaBarraStrumenti(), BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        add(creaPannelloDettaglio(), BorderLayout.SOUTH);
        mostraCatalogo();
    }

    private JPanel creaPannelloDettaglio() {
        JPanel pannello = new JPanel(new BorderLayout());
        pannello.setBorder(BorderFactory.createTitledBorder("Descrizione"));
        dettaglioDescrizione.setEditable(false);
        dettaglioDescrizione.setLineWrap(true);
        dettaglioDescrizione.setWrapStyleWord(true);
        dettaglioDescrizione.setOpaque(false);
        pannello.add(new JScrollPane(dettaglioDescrizione), BorderLayout.CENTER);
        return pannello;
    }

    private void aggiornaDescrizione() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            dettaglioDescrizione.setText("");
            return;
        }
        ProdottoDTO prodotto = modello.prodottoA(tabella.convertRowIndexToModel(riga));
        dettaglioDescrizione.setText(prodotto.descrizione());
        dettaglioDescrizione.setCaretPosition(0);
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        JButton cerca = new JButton("Cerca");
        JButton offerte = new JButton("Offerte");
        JButton tutto = new JButton("Mostra tutto");
        JButton aggiungi = new JButton("Aggiungi al carrello");
        cerca.addActionListener(evento -> cerca());
        offerte.addActionListener(evento -> mostraOfferte());
        tutto.addActionListener(evento -> mostraCatalogo());
        aggiungi.addActionListener(evento -> aggiungiAlCarrello());
        barra.add(campoRicerca);
        barra.add(cerca);
        barra.addSeparator();
        barra.add(offerte);
        barra.add(tutto);
        barra.addSeparator();
        barra.add(aggiungi);
        return barra;
    }

    private void mostraCatalogo() {
        modello.impostaProdotti(catalogo.consultaCatalogo());
    }

    private void mostraOfferte() {
        EsitoDTO<List<ProdottoDTO>> esito = catalogo.visualizzaOfferte();
        aggiornaTabella(esito, "Offerte");
    }

    private void cerca() {
        String termine = campoRicerca.getText().trim();
        if (termine.isEmpty()) {
            mostraCatalogo();
            return;
        }
        aggiornaTabella(catalogo.cercaProdotto(termine), "Ricerca");
    }

    private void aggiornaTabella(EsitoDTO<List<ProdottoDTO>> esito, String titolo) {
        if (esito.successo()) {
            modello.impostaProdotti(esito.dato());
        } else {
            modello.impostaProdotti(List.of());
            JOptionPane.showMessageDialog(this, esito.messaggio(), titolo, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aggiungiAlCarrello() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un prodotto.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ProdottoDTO prodotto = modello.prodottoA(tabella.convertRowIndexToModel(riga));
        String input = JOptionPane.showInputDialog(this,
                "Quantità da aggiungere per \"" + prodotto.nome() + "\":", "1");
        if (input == null) {
            return;
        }
        int quantita;
        try {
            quantita = Integer.parseInt(input.trim());
        } catch (NumberFormatException eccezione) {
            JOptionPane.showMessageDialog(this, "Quantità non valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        EsitoDTO<CarrelloDTO> esito = carrello.aggiungiAlCarrello(clienteId, prodotto.id(), quantita);
        JOptionPane.showMessageDialog(this, esito.messaggio(),
                esito.successo() ? "Carrello" : "Errore",
                esito.successo() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        if (esito.successo()) {
            dopoAggiunta.run();
        }
    }
}
