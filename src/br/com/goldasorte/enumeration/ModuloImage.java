package br.com.goldasorte.enumeration;

public enum ModuloImage {
	COMUM("Comum"),
	FUNCIONARIO("Funcionario");
	
	private String descricao;
		
	ModuloImage(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
