package br.com.goldasorte.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name="seqHorario", sequenceName= "seq_Horario", allocationSize = 1)
public class HorarioFuncionario implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3201170008781540890L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqHorario")
	private Long id;
	
	@Temporal(value=TemporalType.TIME)
	private Date horaEntradaManha;
	
	@Temporal(value=TemporalType.TIME)
	private Date horaSaidaManha;
	
	@Temporal(value=TemporalType.TIME)
	private Date horaEntradaTarde;
	
	@Temporal(value=TemporalType.TIME)
	private Date horaSaidaTarde;
	
	@Temporal(value=TemporalType.DATE)
	private Date dataCriacao;
	
	@Column(columnDefinition="BOOLEAN DEFAULT false")
	private boolean doisTurnos = false;
	
	public HorarioFuncionario(){
		this.doisTurnos = false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof HorarioFuncionario)
			if(((HorarioFuncionario)obj).getId().equals(this.id)) return true;
		
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

	public Date getHoraEntradaManha() {
		return horaEntradaManha;
	}

	public void setHoraEntradaManha(Date horaEntradaManha) {
		this.horaEntradaManha = horaEntradaManha;
	}

	public Date getHoraSaidaManha() {
		return horaSaidaManha;
	}

	public void setHoraSaidaManha(Date horaSaidaManha) {
		this.horaSaidaManha = horaSaidaManha;
	}

	public Date getHoraEntradaTarde() {
		return horaEntradaTarde;
	}

	public void setHoraEntradaTarde(Date horaEntradaTarde) {
		this.horaEntradaTarde = horaEntradaTarde;
	}

	public Date getHoraSaidaTarde() {
		return horaSaidaTarde;
	}

	public void setHoraSaidaTarde(Date horaSaidaTarde) {
		this.horaSaidaTarde = horaSaidaTarde;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public boolean isDoisTurnos() {
		return doisTurnos;
	}

	public void setDoisTurnos(boolean doisTurnos) {
		this.doisTurnos = doisTurnos;
	}

	
	
	
	

}
