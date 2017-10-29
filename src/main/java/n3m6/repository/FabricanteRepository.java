package n3m6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import n3m6.entity.Fabricante;

public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {
	
	public List<Fabricante> findByNomeContainingIgnoreCase(String nome);
	
	public Integer countByNomeContainingIgnoreCase(String nome);
}
