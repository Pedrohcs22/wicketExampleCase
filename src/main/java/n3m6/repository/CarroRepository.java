package n3m6.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import n3m6.entity.Carro;

public interface CarroRepository extends JpaRepository<Carro, Integer> {
	
	public Integer countByPlacaLike(String placa);
	
	public long count();
	
	public Carro getByPlaca(String placa);
}
