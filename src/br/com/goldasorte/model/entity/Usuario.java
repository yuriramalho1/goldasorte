package br.com.goldasorte.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.goldasorte.enumeration.PerfilAcesso;
import br.com.goldasorte.util.SHA;



@Entity
@SequenceGenerator(name = "seqUsuario", sequenceName = "seq_usuario", allocationSize = 1)
public class Usuario implements Serializable {

	private static final long serialVersionUID = -3231596325593106595L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUsuario")
	private Long id;

	@Column(name = "LOGIN", length = 20, nullable = false, columnDefinition = "VARCHAR(20)")
	private String login;

	@Column(nullable=false, columnDefinition = "TEXT")
	private String senha;

	@Temporal(TemporalType.DATE)
	private Date dataCadastro;
	
	@Enumerated(EnumType.STRING)
	private PerfilAcesso perfil;
	
	@Column(nullable=false)
	private String nome;
	
	@Column(nullable=false)
	private String sobrenome;
	
	@Column
	private String cpf;
	
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	private boolean primeiroAcesso;
	
	private boolean ativo;
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Usuario)
			if(((Usuario)obj).getId().equals(this.id)) return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
	public Usuario() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		if(senha != null && !senha.trim().isEmpty())
			this.senha = SHA.bytesToHex(SHA.hash256(senha));
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}


	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public PerfilAcesso getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilAcesso perfil) {
		this.perfil = perfil;
	}

	public boolean isPrimeiroAcesso() {
		return primeiroAcesso;
	}

	public void setPrimeiroAcesso(boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	
	

}


	
