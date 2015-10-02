package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class LightFtpObject implements Serializable{

	private ArrayList<String> arquivos;
	private byte[] arquivo;
	private String nomeArquivo;
	private Integer pedido;	
	
	public LightFtpObject(){
		this.arquivos = new ArrayList<String>();
		this.nomeArquivo = new String();
	}
	
	public LightFtpObject(String diretorio){
		this.arquivos = new ArrayList<String>();
		this.nomeArquivo = new String();
	}
	
	public ArrayList<String> getArquivos() {
		return arquivos;
	}
	
	public void setArquivos(ArrayList<String> arquivos) {
		this.arquivos = arquivos;
	}
	
	public byte[] getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public void criarNomesArquivos(String diretorio) {
		this.arquivos = new ArrayList<String>();
		File file = new File(diretorio);
		File afile[] = file.listFiles();
		
		for (int i = 0; i < afile.length; i++) {
			File arquivo = new File(afile[i].toString());
			arquivos.add(arquivo.getName());
		}
		
		java.util.Collections.sort(arquivos);
	}

	public Integer getPedido() {
		return pedido;
	}

	public void setPedido(Integer pedido) {
		this.pedido = pedido;
	}
}
