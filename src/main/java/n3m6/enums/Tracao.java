package n3m6.enums;

public enum Tracao {
	COMBUSTAO("Combustão"), ELETRICO("Elétrico");
	
	private String nome;
	
	Tracao(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
