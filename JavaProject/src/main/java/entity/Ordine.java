package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ordine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private LocalDateTime dataCreazione;
	private double totaleComplessivo;
	private String indirizzoSpedizione;

	@Enumerated(EnumType.STRING)
	private StatoOrdine stato;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "ordine_id")
	private List<RigaOrdine> righe = new ArrayList<RigaOrdine>();

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
