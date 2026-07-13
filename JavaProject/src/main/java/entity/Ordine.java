package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ordine {
	private long id;
	private LocalDateTime dataCreazione;
	private double totaleComplessivo;
	private String indirizzoSpedizione;
	private StatoOrdine stato;
	private Cliente cliente;
	private ArrayList<RigaOrdine> righe = new ArrayList<RigaOrdine>();

	protected Ordine() {}

	public Ordine(Cliente cliente) {
		this.dataCreazione = LocalDateTime.now();
		this.indirizzoSpedizione = cliente.getIndirizzo();
		this.stato = StatoOrdine.INSERITO;
		this.cliente = cliente;
	}

	public void aggiungiRiga(RigaOrdine riga) {
		righe.add(riga);
		this.totaleComplessivo += riga.calcolaSubtotale();
	}

	public List<Prodotto> scaricaMagazzino() {
			List<Prodotto> scaricati = new ArrayList<>();
			for (RigaOrdine riga : righe) {
				riga.scarica();
				scaricati.add(riga.getProdotto());
			}
			return scaricati;
	}

	//Getter
	public long getId() {return id;}

	public LocalDateTime getDataCreazione() {return dataCreazione;}

	public double getTotaleComplessivo() {return totaleComplessivo;}

	public String getIndirizzoSpedizione() {return indirizzoSpedizione;}

	public StatoOrdine getStato() {return stato;}
}
