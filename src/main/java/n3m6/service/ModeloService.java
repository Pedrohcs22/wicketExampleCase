package n3m6.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import n3m6.entity.Modelo;
import n3m6.repository.ModeloRepository;

@Service
public class ModeloService {

	@Autowired
	private ModeloRepository repository;

	public List<Modelo> listar() {
		return repository.findAll();
	}
}
