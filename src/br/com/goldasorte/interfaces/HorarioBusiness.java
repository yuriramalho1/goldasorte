package br.com.goldasorte.interfaces;

import br.com.goldasorte.dao.IGenericDAO;
import br.com.goldasorte.model.entity.HorarioFuncionario;

public interface HorarioBusiness extends IGenericDAO<HorarioFuncionario> {
	
	public HorarioFuncionario salvar(HorarioFuncionario horario);

}
