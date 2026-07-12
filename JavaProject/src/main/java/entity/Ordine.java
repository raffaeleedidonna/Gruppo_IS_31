package entity;

import java.util.ArrayList;
import entity.RigaOrdine;

public class Ordine {
	private long id;
	private LocalDateTime dataCreazione;
	private double totaleComplessivo;
	private String indirizzoSpedizione;
	private StatoOrdine stato;
	public Cliente cliente;
	public ArrayList<RigaOrdine> righe = new ArrayList<RigaOrdine>();

	protected Ordine() {
		throw new UnsupportedOperationException();
	}

	public Ordine(Cliente cliente, String indirizzoSpedizione) {
		throw new UnsupportedOperationException();
	}

	public double calcolaTotale() {
		throw new UnsupportedOperationException();
	}

	public boolean aggiungiRiga(Prodotto prodotto, long quantita, double prezzoCorrente) {
		throw new UnsupportedOperationException();
	}
}