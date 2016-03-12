package br.com.goldasorte.interfaces;

import java.util.List;

import br.com.goldasorte.dao.IGenericDAO;
import br.com.goldasorte.model.entity.Funcionario;



public interface FuncionarioBusiness extends IGenericDAO<Funcionario> {

	public Funcionario salvar(Funcionario funcionario) throws Exception;
	public void verificaMatricula(Funcionario funcionario) throws Exception;
	public List<Funcionario>getFuncionariosAtivos() throws Exception;
}
