package br.com.goldasorte.backbean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.goldasorte.model.entity.Usuario;
import br.com.goldasorte.util.NavigationUtil;
import br.com.goldasorte.util.SessionControl;




@ManagedBean
@javax.faces.bean.SessionScoped
public class SessionBean implements Serializable {
	

	private static final long serialVersionUID = 2598510283673645554L;
	
	@Inject private SessionControl session;
	
	private String ip;
	
	@PostConstruct
	public void init(){
			FacesContext fc = FacesContext.getCurrentInstance();  
		    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();  
		    ip = (request.getRemoteAddr());
	}
	
	public String nomeUsuarioLogado(){
		Usuario user = (Usuario) getSession().getAttributeSession(SessionControl.USUARIO);
		if(user != null)
			return user.getNome();

		return null;
	}

	public Usuario login(String login, String senha){
		return getSession().login(login, senha);
	}
	
	public void logout() throws IOException {
		getSession().logout();
	}
	
	public boolean isAdmin() {
		return getSession().isAdmin();
	}
	
	public boolean isPerfil(){
		return getSession().isPerfil();
	}
	public boolean loggedIn(){
		return getSession().loggedIn();
	}

	public void listaUsuarios() {
		NavigationUtil.toUsuario();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the session
	 */
	public SessionControl getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(SessionControl session) {
		this.session = session;
	}
}