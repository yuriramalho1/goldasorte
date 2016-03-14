package br.com.goldasorte.backbean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.goldasorte.model.entity.Usuario;
import br.com.goldasorte.util.FacesUtil;
import br.com.goldasorte.util.NavigationUtil;
import br.com.goldasorte.util.SessionControl;


//@Named
@ManagedBean
@javax.faces.bean.ViewScoped
public class LoginBean implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -9075096264667547603L;

	private Usuario usuario;

	@Inject	
	private SessionControl session;

	
	
	public LoginBean() {
		usuario = new Usuario();
	}

	public void logar() {

		usuario = session.login(usuario.getLogin(), usuario.getSenha());

	}
	
public String paginaRed(){
	if (usuario != null) {
		verificaAcesso();
		if (usuario.isPrimeiroAcesso())
			return "".concat(NavigationUtil.TO_PRIMEIRO_ACESSO.concat(".jsf"));
		else
			return "/".concat(NavigationUtil.TO_PAGES_HOME.concat(".jsf"));
	} else {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Login ou senha inválidos", ""));
		usuario = new Usuario();
		return "";
	}
	
}
	public void redirecionaConsulta(){
		FacesUtil.redirectTo("consultaGeral");
	}
	
	public void redirecionaEstatisticas(){
		FacesUtil.redirectTo("dadosEstatisticos");
	}
	private void verificaAcesso() {
		/*if (usuario.isPrimeiroAcesso())
			NavigationUtil.toPrimeiroAcesso();
		else
			NavigationUtil.toPagesHome();*/

	}

	public void logout() {
		session.invalidateSession();
		NavigationUtil.toLogin();
	}

	public Usuario getUsuarioLogado() {
		return session.getUsuarioSession();
	}
	
	public void voltarLogin() {
		FacesUtil.redirectTo("login");

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
