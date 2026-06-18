package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import it.ecommerce.control.GestioneOrdiniController;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.StatoOrdineDTO;

public class ElencoOrdiniView extends JPanel {

    private final GestioneOrdiniController controller;
    private final ModelloTabellaOrdini modello = new ModelloTabellaOrdini();
    private final JTable tabella = new JTable(modello);
    private final JLabel etichettaStato = new JLabel(" ");

    public ElencoOrdiniView(GestioneOrdiniController controller) {
        super(new BorderLayout());
        this.controller = controller;
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(creaBarraStrumenti(), BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        etichettaStato.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(etichettaStato, BorderLayout.SOUTH);
        ricarica();
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        JButton dettaglio = new JButton("Dettaglio");
        JButton stato = new JButton("Aggiorna stato");
        JButton aggiorna = new JButton("Aggiorna");
        dettaglio.addActionListener(evento -> mostraDettaglio());
        stato.addActionListener(evento -> aggiornaStato());
        aggiorna.addActionListener(evento -> ricarica());
        barra.add(dettaglio);
        barra.add(stato);
        barra.add(aggiorna);
        return barra;
    }

    private void ricarica() {
        EsitoDTO<List<OrdineDTO>> esito = controller.elencoOrdini();
        if (esito.successo()) {
            modello.imposta(esito.dato());
            etichettaStato.setText(" ");
        } else {
            modello.imposta(List.of());
            etichettaStato.setText(esito.messaggio());
        }
    }

    private OrdineDTO selezionato() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un ordine.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return modello.ordineA(tabella.convertRowIndexToModel(riga));
    }

    private void mostraDettaglio() {
        OrdineDTO selezionato = selezionato();
        if (selezionato == null) {
            return;
        }
        OrdineDTO dettaglio = controller.dettaglioOrdine(selezionato.id());
        if (dettaglio == null) {
            JOptionPane.showMessageDialog(this, "Ordine non disponibile.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new DettaglioOrdineDialog(SwingUtilities.getWindowAncestor(this), dettaglio).setVisible(true);
    }

    private void aggiornaStato() {
        OrdineDTO selezionato = selezionato();
        if (selezionato == null) {
            return;
        }
        StatoOrdineDTO nuovoStato = (StatoOrdineDTO) JOptionPane.showInputDialog(this,
                "Nuovo stato per l'ordine numero " + selezionato.id() + ":",
                "Aggiorna stato", JOptionPane.QUESTION_MESSAGE, null,
                StatoOrdineDTO.values(), selezionato.stato());
        if (nuovoStato == null) {
            return;
        }
        EsitoDTO<OrdineDTO> esito = controller.aggiornaStato(selezionato.id(), nuovoStato);
        JOptionPane.showMessageDialog(this, esito.messaggio(),
                esito.successo() ? "Operazione riuscita" : "Errore",
                esito.successo() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        ricarica();
    }
}
