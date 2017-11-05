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

@Entity
public @Data class Fabricante implements Serializable {

	@Id
	@GeneratedValue(generator = "fabricante", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "fabricante", sequenceName = "fabricante_sequence", allocationSize = 1)
	private Integer id;

	@NotNull
	@NotBlank
	private String nome;

	@NotNull
	@NotBlank
	private String pais;

}
