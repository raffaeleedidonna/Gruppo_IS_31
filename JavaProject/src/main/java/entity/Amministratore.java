package entity;

import jakarta.persistence.*;

@Entity
public class Amministratore extends Utente {

	public Ruolo ruolo() {
		return Ruolo.AMMINISTRATORE;
	}
}
