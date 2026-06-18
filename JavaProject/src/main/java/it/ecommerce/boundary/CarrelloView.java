package it.ecommerce.boundary;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import it.ecommerce.control.CarrelloController;
import it.ecommerce.control.OrdineClienteController;
import it.ecommerce.control.dto.CarrelloDTO;
import it.ecommerce.control.dto.EsitoDTO;
import it.ecommerce.control.dto.OrdineDTO;
import it.ecommerce.control.dto.RigaCarrelloDTO;

public class CarrelloView extends JPanel {

    private final CarrelloController controller;
    private final OrdineClienteController ordini;
    private final Long clienteId;
    private final Runnable dopoOrdine;
    private final ModelloTabellaCarrello modello = new ModelloTabellaCarrello();
    private final JTable tabella = new JTable(modello);
    private final JLabel etichettaTotale = new JLabel("Totale: € 0.00");

    public CarrelloView(CarrelloController controller, OrdineClienteController ordini,
                        Long clienteId, Runnable dopoOrdine) {
        super(new BorderLayout());
        this.controller = controller;
        this.ordini = ordini;
        this.clienteId = clienteId;
        this.dopoOrdine = dopoOrdine;
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(creaBarraStrumenti(), BorderLayout.NORTH);
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        etichettaTotale.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(etichettaTotale, BorderLayout.SOUTH);
        ricarica();
    }

    public void ricarica() {
        CarrelloDTO carrello = controller.carrello(clienteId);
        modello.imposta(carrello.righe());
        etichettaTotale.setText("Totale: € " + carrello.totale().toPlainString());
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        JButton modifica = new JButton("Modifica quantità");
        JButton rimuovi = new JButton("Rimuovi");
        JButton aggiorna = new JButton("Aggiorna");
        JButton conferma = new JButton("Conferma ordine");
        modifica.addActionListener(evento -> modificaQuantita());
        rimuovi.addActionListener(evento -> rimuovi());
        aggiorna.addActionListener(evento -> ricarica());
        conferma.addActionListener(evento -> confermaOrdine());
        barra.add(modifica);
        barra.add(rimuovi);
        barra.add(aggiorna);
        barra.addSeparator();
        barra.add(conferma);
        return barra;
    }

    private void confermaOrdine() {
        EsitoDTO<OrdineDTO> esito = ordini.confermaOrdine(clienteId, null);
        JOptionPane.showMessageDialog(this, esito.messaggio(),
                esito.successo() ? "Ordine confermato" : "Avviso",
                esito.successo() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        ricarica();
        dopoOrdine.run();
    }

    private void modificaQuantita() {
        RigaCarrelloDTO riga = rigaSelezionata();
        if (riga == null) {
            return;
        }
        String input = JOptionPane.showInputDialog(this,
                "Nuova quantità per \"" + riga.prodottoNome() + "\":", riga.quantita());
        if (input == null) {
            return;
        }
        int quantita;
        try {
            quantita = Integer.parseInt(input.trim());
        } catch (NumberFormatException eccezione) {
            JOptionPane.showMessageDialog(this, "Quantità non valida.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        applica(controller.modificaQuantita(clienteId, riga.id(), quantita));
    }

    private void rimuovi() {
        RigaCarrelloDTO riga = rigaSelezionata();
        if (riga == null) {
            return;
        }
        applica(controller.modificaQuantita(clienteId, riga.id(), 0));
    }

    private void applica(EsitoDTO<CarrelloDTO> esito) {
        if (!esito.successo()) {
            JOptionPane.showMessageDialog(this, esito.messaggio(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
        ricarica();
    }

    private RigaCarrelloDTO rigaSelezionata() {
        int riga = tabella.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un elemento del carrello.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return modello.rigaA(tabella.convertRowIndexToModel(riga));
    }

    private static final class ModelloTabellaCarrello extends AbstractTableModel {

        private final String[] colonne = {"Prodotto", "Prezzo", "Quantità", "Disponibile", "Subtotale"};
        private List<RigaCarrelloDTO> righe = new ArrayList<>();

        void imposta(List<RigaCarrelloDTO> nuove) {
            this.righe = new ArrayList<>(nuove);
            fireTableDataChanged();
        }

        RigaCarrelloDTO rigaA(int indice) {
            return righe.get(indice);
        }

        @Override
        public int getRowCount() {
            return righe.size();
        }

        @Override
        public int getColumnCount() {
            return colonne.length;
        }

        @Override
        public String getColumnName(int colonna) {
            return colonne[colonna];
        }

        @Override
        public Object getValueAt(int riga, int colonna) {
            RigaCarrelloDTO elemento = righe.get(riga);
            return switch (colonna) {
                case 0 -> elemento.prodottoNome();
                case 1 -> elemento.prezzoAttuale();
                case 2 -> elemento.quantita();
                case 3 -> elemento.quantitaDisponibile();
                case 4 -> elemento.prezzoAttuale().multiply(BigDecimal.valueOf(elemento.quantita()));
                default -> "";
            };
        }
    }
}
