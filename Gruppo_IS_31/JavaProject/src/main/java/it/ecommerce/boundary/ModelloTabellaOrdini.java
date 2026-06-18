package it.ecommerce.boundary;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import it.ecommerce.control.dto.OrdineDTO;

final class ModelloTabellaOrdini extends AbstractTableModel {

    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final String[] colonne = {"Numero", "Data", "Totale", "Stato"};
    private List<OrdineDTO> ordini = new ArrayList<>();

    void imposta(List<OrdineDTO> nuovi) {
        this.ordini = new ArrayList<>(nuovi);
        fireTableDataChanged();
    }

    OrdineDTO ordineA(int indice) {
        return ordini.get(indice);
    }

    @Override
    public int getRowCount() {
        return ordini.size();
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
        OrdineDTO ordine = ordini.get(riga);
        return switch (colonna) {
            case 0 -> ordine.id();
            case 1 -> ordine.dataCreazione() == null ? "" : formato.format(ordine.dataCreazione());
            case 2 -> "€ " + ordine.totaleComplessivo();
            case 3 -> ordine.stato();
            default -> "";
        };
    }
}
