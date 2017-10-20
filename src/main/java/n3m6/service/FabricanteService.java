package n3m6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import n3m6.entity.Fabricante;
import n3m6.repository.FabricanteRepository;

@Service
public class FabricanteService {

	@Autowired
	private FabricanteRepository repository;

	public List<Fabricante> listar() {
		return repository.findAll();
	}

	public Fabricante obter(Integer id) {
		return repository.findOne(id);
	}

	@Transactional
	public Fabricante salvar(Fabricante fabricante) {
		return repository.save(fabricante);
	}

	@Transactional
	public void remover(Integer id) {
		repository.delete(id);
	}

}
