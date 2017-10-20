package n3m6.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(of = "id")
@SuppressWarnings("serial")
public @Data class Carro implements Serializable {

  @Id
  @GeneratedValue(generator = "carro", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "carro", sequenceName = "carro_sequence", allocationSize = 1)
  private Integer id;

  private String placa;
  
  @ManyToOne
  private Modelo modelo;

  private int tracao;
 
}
