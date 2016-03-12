package br.com.goldasorte.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@SequenceGenerator(name="seqFuncionario", sequenceName = "seq_Funcionario", allocationSize = 1)
public class Funcionario  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 787155985360198200L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqFuncionario")
	private Long id;
	
	@Column(nullable=false, length = 255)
	private String nome;
	
	@Column(nullable=false, length = 14)
	private String cpf;
	
	@Column(nullable=false, length = 35)
	private String matricula;
	
	@Column
	private String telefone;
	
	@Column
	private String email;
	
	@Column
	private String endereco;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_horario", referencedColumnName = "id")
	private HorarioFuncionario horario;
	
	
	@Column
	private String tipoFoto;
	
	@Column
	private Integer turnos;
	
	@Column
	private boolean ativo;
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Funcionario)
			if(((Funcionario)obj).getId().equals(this.id)) return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf.replaceAll("\\.","").replaceAll("\\/","").replace("-","");
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula.replaceAll("\\.","").replaceAll("\\/","").replace("-","");
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}


	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getTipoFoto() {
		return tipoFoto;
	}

	public void setTipoFoto(String tipoFoto) {
		this.tipoFoto = tipoFoto;
	}

	public Integer getTurnos() {
		return turnos;
	}

	public void setTurnos(Integer turnos) {
		this.turnos = turnos;
	}

	public HorarioFuncionario getHorario() {
		return horario;
	}

	public void setHorario(HorarioFuncionario horario) {
		this.horario = horario;
	}

	
	
}
