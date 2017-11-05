package n3m6.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(of = "id")
public @Data class Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "modelo", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "modelo", sequenceName = "modelo_sequence", allocationSize = 1)
	private Integer id;

	@NotNull
	@NotBlank
	private String descricao;
	
	public Modelo() {
		super();
	}

	@Override
	public String toString() {
		return descricao;
	}

}
