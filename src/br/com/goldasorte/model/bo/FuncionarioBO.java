package br.com.goldasorte.model.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.goldasorte.dao.GenericHibernateDAO;
import br.com.goldasorte.interfaces.FuncionarioBusiness;
import br.com.goldasorte.model.entity.Funcionario;

public class FuncionarioBO extends GenericHibernateDAO<Funcionario> implements FuncionarioBusiness, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6732882041470851795L;
	
	
	public FuncionarioBO(){
		super(Funcionario.class);
	}
	
	@Override
	public Funcionario salvar(Funcionario funcionario) throws Exception{
		if(funcionario.getId() == null){
			this.verificaMatricula(funcionario);
			super.create(funcionario);
		}
		else
			super.update(funcionario);
		
		return funcionario;
	}
	
	
	@Override
	public void verificaMatricula(Funcionario funcionario) throws Exception{
		try{
			Query queryFuc = em.createQuery("SELECT f FROM Funcionario f WHERE f.matricula =:matricula AND f.ativo =:ativo");
			queryFuc.setParameter("matricula", funcionario.getMatricula());
			queryFuc.setParameter("ativo", true);
			Number matriculas = (Number) queryFuc.getSingleResult();
			
			if (matriculas.longValue() > 0  ) {
				throw new Exception();
			}
		} catch (NoResultException e) {
			return;
		} catch (Exception e) {
			throw new Exception(
					"Essa matrícula pertence a outro funcionário");
		}
		
	}
	
	@Override
	public List<Funcionario>getFuncionariosAtivos() throws Exception {
		return em.createQuery("SELECT f FROM Funcionario f WHERE f.ativo =:ativo ").setParameter("ativo", true).getResultList();
	}
	public List<Funcionario>funcionarioPorMat(String str){
		return em.createQuery("SELECT f FROM Funcionario f WHERE f.ativo =:ativo AND f.cpf =:cpf OR f.matricula =:matricula ")
				.setParameter("ativo", true).setParameter("cpf", str).setParameter("matricula", str).getResultList();
		
	}
	
	
	public List <Funcionario>funcionarioFicha(String funcionarioSelecionado){
		TypedQuery<Funcionario> query = super.em.createQuery("SELECT f FROM Funcionario f WHERE f.cpf =:cpf OR f.matricula =:matricula", Funcionario.class);
		query.setParameter("cpf", funcionarioSelecionado);
		query.setParameter("matricula", funcionarioSelecionado);
		
		return query.getResultList();
			
			
		}
	
	public List<Funcionario>consultaFuncionario(Funcionario funcionario){
		Map<String, Object> paramentros = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder("SELECT f FROM Funcionario f WHERE 1=1 AND f.ativo=true");
		
		if(funcionario.getNome() != null && !funcionario.getNome().isEmpty()){
			query.append(" AND LOWER(f.nome) LIKE LOWER (:nome)");
			paramentros.put("nome", "%"+funcionario.getNome()+"%");
		}
		
		if(funcionario.getMatricula() != null && !funcionario.getMatricula().isEmpty()){
			query.append(" AND f.matricula =:matricula");
			paramentros.put("matricula", funcionario.getMatricula());
		}
		
		if(funcionario.getCpf() != null && !funcionario.getCpf().isEmpty()){
			query.append(" AND f.cpf =:cpf)");
			paramentros.put("cpf", funcionario.getCpf());
		}
		
		TypedQuery<Funcionario> sec = em.createQuery(query.toString(), Funcionario.class);
		if(paramentros != null && !paramentros.isEmpty()){
		for(Entry<String, Object> entry : paramentros.entrySet()){
			sec.setParameter(entry.getKey(), entry.getValue());
		}
		}
	
		return sec.getResultList();
	}


	
}
