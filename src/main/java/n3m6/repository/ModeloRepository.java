package n3m6.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n3m6.entity.Modelo;

public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
	
	public Modelo getByDescricao(String descricao);
}
