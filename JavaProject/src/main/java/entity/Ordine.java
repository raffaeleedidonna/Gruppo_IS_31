package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import entity.RigaOrdine;

public class Ordine {
	private long id;
	private LocalDateTime dataCreazione;
	private double totaleComplessivo;
	private String indirizzoSpedizione;
	private StatoOrdine stato;
	private Cliente cliente;
	private ArrayList<RigaOrdine> righe = new ArrayList<RigaOrdine>();

	public long getId() {return id;}
	public LocalDateTime getDataCreazione() {return dataCreazione;}
	public double getTotaleComplessivo() {return totaleComplessivo;}
	public String getIndirizzoSpedizione() {return indirizzoSpedizione;}
	public StatoOrdine getStato() {return stato;}


	protected Ordine() {

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