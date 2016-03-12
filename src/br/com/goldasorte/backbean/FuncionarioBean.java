package br.com.goldasorte.backbean;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.swing.ImageIcon;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.goldasorte.enumeration.ModuloImage;
import br.com.goldasorte.enumeration.TipoFuncionario;
import br.com.goldasorte.model.bo.FuncionarioBO;
import br.com.goldasorte.model.bo.HorarioBO;
import br.com.goldasorte.model.entity.Funcionario;
import br.com.goldasorte.model.entity.HorarioFuncionario;
import br.com.goldasorte.util.FacesUtil;
import br.com.goldasorte.util.ImageUtils;
import br.com.goldasorte.util.Util;




@ManagedBean
@javax.faces.bean.ViewScoped
public class FuncionarioBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 951778153629525502L;
	
	
	private Funcionario funcionario;
	
	private List <Funcionario>listaFuncionarioFicha;
	private String funcionarioSelecionado;
	private Funcionario funcionarioFicha;
	private Funcionario funcionarioBiometria;
	
	private boolean turnoDois;
	
	
	@Inject
	private FuncionarioBO funcionarioBO;
	
	
	
	
	
	@Inject
	private HorarioBO horarioBO;
	
	private HorarioFuncionario horario;
	
	
	
	
	
	private List<Funcionario> funcionarios;
	
	private String param;
	
	private LazyDataModel<Funcionario>lazyModelFunc;
	private int quantidadeLinhas;
	
	private List<Funcionario>FuncionariosAtivo;
	
	private Date date = new Date();
	private DateFormat formato = new SimpleDateFormat("HH:mm");
	private String horaAtual;
	
	

	private boolean funcEncontrado;
	
	private UploadedFile file;
	private String loteImagem;
	private InputStream inputStream;
	private StreamedContent uploadedFileAsStream;
	
	
	@PostConstruct
	public void Inicialization() throws Exception{
		if(funcionario == null){
			funcionario = new Funcionario();
		}
		
		
		if(horario == null){
			horario = new HorarioFuncionario();
		}
		this.consultarFuncionario();
		
	}
	
	public void limpaBean(){
		funcionario = new Funcionario();
		horario = new HorarioFuncionario();
	}
	
	
	public void redirect(){
		funcionario = new Funcionario();
		FacesUtil.redirectTo("cadastroFuncionario");
	}
	
	public void cancelar(){
		funcionario = new Funcionario();
		FacesUtil.redirectTo("funcionarios");
	}
	
	
	public void redirectProf(){
		FacesUtil.redirectTo("cadastro-profissao");
	}
	
	public void salvarFuncionario(){
		Calendar dataAtual = Calendar.getInstance();
				
		try{
			if(funcionario.getNome().trim().isEmpty())
				throw new Exception("O nome está vazio!");
			
			if(funcionario.getId() == null)
				horario.setDataCriacao(dataAtual.getTime());
				if(horario.getHoraEntradaTarde() != null && horario.getHoraSaidaTarde() != null){
					horario.setDoisTurnos(true);
					funcionario.setTurnos(2);
				}	
				if(horario.getHoraEntradaTarde() == null && horario.getHoraSaidaTarde() == null){
					horario.setDoisTurnos(false);
					funcionario.setTurnos(1);
				}
				horarioBO.salvar(horario);
				funcionario.setAtivo(true);
				funcionario.setHorario(horario);
				funcionarioBO.salvar(funcionario);
				
				this.upload(funcionario);
			this.limpaBean();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Os dados foram salvos com sucesso", "Dados cadastrados."));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	
	
	
	public void desabilitaFuncionario(){
		
		try {
			funcionario.setAtivo(false);
			funcionarioBO.salvar(funcionario);
			this.limpaBean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Funcionário foi excluído", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public List<SelectItem> tipoFuncionario(){
		List<SelectItem> itens = new ArrayList<SelectItem>();
			itens.add(new SelectItem("Selecione"));
		for(TipoFuncionario tipoFuncionario : TipoFuncionario.values())
			itens.add(new SelectItem(tipoFuncionario, tipoFuncionario.getDescricao()));
		
		return itens;
		
	}
	
	
	public void consultarFuncionario() throws Exception {
		funcionarios = funcionarioBO.consultaFuncionario(funcionario);
		if(funcionarios == null || funcionarios.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							" Nenhum funcionário foi encontrado.", ""));
		}
	}
	
	
	
	private void redimencionaImagem(String caminho) {
		Image imagem = null;

		try {
			imagem = new ImageIcon(caminho).getImage();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int tipo = ImageUtils.IMAGE_JPEG;

		try {
			ImageUtils.saveImage(
					ImageUtils.resizeImageProportional(imagem, tipo, 650),
					caminho, tipo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean existeImagem() throws IOException {
		String folder_file = Util.getPastaFotos() + File.separator + this.funcionario.getId() + File.separator + ModuloImage.FUNCIONARIO.toString() + "." + this.funcionario.getTipoFoto();
		File foto = new File(folder_file);
		return foto.exists();
	}
	
	private void upload(Funcionario funcionario) {

		if (file != null) {

			String folder_file = Util.getPastaFotos() + File.separator
					+ funcionario.getId();

			try { 
				if((new File(folder_file)).exists()){
					new File(folder_file).delete();
				}
				File targetFolder = new File(folder_file);
				targetFolder.mkdirs();

				String[] extensao = file.getContentType().split("/");
				String arquivo = ModuloImage.FUNCIONARIO.toString() + "."
						+ extensao[1];

				OutputStream out = new FileOutputStream(new File(targetFolder,
						arquivo));

				int read = 0;
				byte[] bytes = new byte[2048];

				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				inputStream.close();
				out.flush();
				out.close();

				funcionario.setTipoFoto(extensao[1]);

				String caminhoCompleto = folder_file + File.separator + arquivo;

				redimencionaImagem(caminhoCompleto);

				funcionarioBO.update(funcionario);
			} catch (IOException e) {
				e.printStackTrace();
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha",
						"Ocorreu uma falha no upload do arquivo.");
			}
		}
	}
	
	public StreamedContent getUploadedFileAsStream() {
		return uploadedFileAsStream;
	}
	
	public void uploadFile(FileUploadEvent event) throws IOException {
		file = event.getFile();
		inputStream = file.getInputstream();
	}
	
	public void homeConsulta() throws Exception{
		List<Funcionario>listaF =  new ArrayList<Funcionario>();
		RequestContext contextRequestContext = RequestContext.getCurrentInstance();
		
		if(funcionarioSelecionado != null)
		listaF = funcionarioBO.funcionarioPorMat(funcionarioSelecionado);
		for(Funcionario f : listaF){
			if(funcionarioSelecionado.equals(f.getMatricula()) || funcionarioSelecionado.equals(f.getCpf())){
				funcionario = f;
				if(funcionario.getHorario().isDoisTurnos() == true){
				this.turnoDois = true;
				}
				this.horaAtual = formato.format(date);
				
				contextRequestContext.execute("PF('pontoDialog').show()");
			}
			
		}
		
	}
	
	public void fichaFuncionario(){
		if(funcionarioSelecionado != null){
		this.listaFuncionarioFicha = funcionarioBO.funcionarioFicha(funcionarioSelecionado); 
		for(Funcionario f : listaFuncionarioFicha){
			if(funcionarioSelecionado.equals(f.getCpf()) || funcionarioSelecionado.equals(f.getMatricula())) {
				funcionarioFicha = f;
			} 
			
			}
		  }
		}
	
	

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getLoteImagem() {
		return loteImagem;
	}

	public void setLoteImagem(String loteImagem) {
		this.loteImagem = loteImagem;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public FuncionarioBO getFuncionarioBO() {
		return funcionarioBO;
	}

	public void setFuncionarioBO(FuncionarioBO funcionarioBO) {
		this.funcionarioBO = funcionarioBO;
	}

	public List<Funcionario> getFuncionariosAtivo() {
		return FuncionariosAtivo;
	}

	public void setFuncionariosAtivo(List<Funcionario> funcionariosAtivo) throws Exception {
		FuncionariosAtivo = funcionariosAtivo;
	}

	public LazyDataModel<Funcionario> getLazyModelFunc() {
		return lazyModelFunc;
	}

	public void setLazyModelFunc(LazyDataModel<Funcionario> lazyModelFunc) {
		this.lazyModelFunc = lazyModelFunc;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) throws Exception {
		try{
			this.param = param;
			if(param != null && !param.isEmpty()){
				this.funcionario = funcionarioBO.selectById(Long.valueOf(param));
				this.horario = horarioBO.selectById(this.funcionario.getHorario().getId());
			}

		} catch (NumberFormatException e) {
			throw new Exception("Formato de parametro ilegal");
		} catch (IllegalArgumentException e) {
			throw new Exception("Argumento ilegal");
		} catch (Exception e) {
			throw new Exception("Erro inesperado!");
		}
	}

	public String getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(String funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado.replaceAll("\\.","").replaceAll("\\/","").replace("-","");
	}

	public Funcionario getFuncionarioFicha() {
		return funcionarioFicha;
	}

	public void setFuncionarioFicha(Funcionario funcionarioFicha) {
		this.funcionarioFicha = funcionarioFicha;
	}

	public boolean isFuncEncontrado() {
		return funcEncontrado;
	}

	public void setFuncEncontrado(boolean funcEncontrado) {
		this.funcEncontrado = funcEncontrado;
	}

	public Funcionario getFuncionarioBiometria() {
		return funcionarioBiometria;
	}

	public void setFuncionarioBiometria(Funcionario funcionarioBiometria) {
		this.funcionarioBiometria = funcionarioBiometria;
	}

	public String getHoraAtual() {
		return horaAtual;
	}

	public void setHoraAtual(String horaAtual) {
		this.horaAtual = horaAtual;
	}


	public HorarioBO getHorarioBO() {
		return horarioBO;
	}

	public void setHorarioBO(HorarioBO horarioBO) {
		this.horarioBO = horarioBO;
	}

	public HorarioFuncionario getHorario() {
		return horario;
	}

	public void setHorario(HorarioFuncionario horario) {
		this.horario = horario;
	}

	public int getQuantidadeLinhas() {
		return quantidadeLinhas;
	}

	public void setQuantidadeLinhas(int quantidadeLinhas) {
		this.quantidadeLinhas = quantidadeLinhas;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}


	public boolean isTurnoDois() {
		return turnoDois;
	}

	public void setTurnoDois(boolean turnoDois) {
		this.turnoDois = turnoDois;
	}
	
	


	
	
	
	
	
	
	
	

}
