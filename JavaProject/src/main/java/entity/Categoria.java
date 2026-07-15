package entity;

import jakarta.persistence.*;

@Entity
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nome;

	protected Categoria() {}

	public Categoria(String nome) {
		this.nome = nome;
	}

	//Getter
	public String getNome() {return nome;}
}
