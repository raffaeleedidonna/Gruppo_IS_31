package it.ecommerce.boundary;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import it.ecommerce.control.dto.ProdottoDTO;

final class ModelloTabellaProdotti extends AbstractTableModel {

    private final String[] colonne = {"Nome", "Categoria", "Prezzo", "Quantità", "Disponibile", "In offerta"};
    private List<ProdottoDTO> prodotti = new ArrayList<>();

    void impostaProdotti(List<ProdottoDTO> nuovi) {
        this.prodotti = new ArrayList<>(nuovi);
        fireTableDataChanged();
    }

    ProdottoDTO prodottoA(int riga) {
        return prodotti.get(riga);
    }

    @Override
    public int getRowCount() {
        return prodotti.size();
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
        ProdottoDTO prodotto = prodotti.get(riga);
        return switch (colonna) {
            case 0 -> prodotto.nome();
            case 1 -> prodotto.categoriaNome();
            case 2 -> prodotto.prezzoAttuale();
            case 3 -> prodotto.quantitaMagazzino();
            case 4 -> prodotto.disponibile() ? "Sì" : "No";
            case 5 -> prodotto.inOfferta() ? "Sì" : "No";
            default -> "";
        };
    }
}
