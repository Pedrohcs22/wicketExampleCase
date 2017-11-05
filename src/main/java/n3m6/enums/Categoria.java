package n3m6.enums;

public enum Categoria {
	PARTICULAR("Particular"), ALUGUEL("Aluguel"), OFICIAL("Oficial");
	
	private String nome;
	
	Categoria(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
