package entity;

import jakarta.persistence.*;

@Entity
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nome;

	//Getter
	public String getNome() {return nome;}
}
