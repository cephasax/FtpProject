package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *	Essa classe define uma estrutura de objeto para que sejam armazenados
 *	e transferidas as informacoes e/ou arquivos de forma correta e simples
 *
 *	Os atributos sao os seguintes:
 *	
 *		>arquivos - 	lista com os nomes de arquivos contidos em determinado diretorio.
 *						E usado para armazenar os nomes de arquivos a partir da do momento
 *						em que uma instancia da classe LightFtpService com um diretorio definido
 *						utiliza o metodo criarNomesArquivos(String diretorio);
 *		
 *		>arquivo -		Conteudo de arquivo definido na interacao entre cliente e servidor.
 *						E armazenado na forma de array de bytes para possibilitar a transferencia
 *						de qualquer tipo de arquivo.
 *
 * 		>nomeArquivo - 	Nome do arquivo que está sendo utilizado no momento
 *
 *		>pedido	-		Numero que representa o servico solicitado pelo cliente ou pelo servidor		
 *
 * @author Cephas Barreto
 *
 */

public class LightFtpObject implements Serializable{

	private ArrayList<String> arquivos;
	private byte[] arquivo;
	private String nomeArquivo;
	private Integer pedido;	
	
	//Construtor
	public LightFtpObject(){
		this.arquivos = new ArrayList<String>();
		this.nomeArquivo = new String();
	}
	
	/*
	 * Metodo que carrega, a cada chamada, a lista de arquivos
	 * de um determinado diretorio(na logica do servico proposto,
	 * para a instancia da classe LightFtpService que o mantem)
	 */
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
	
	//GETTERS e SETTERS
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

	public Integer getPedido() {
		return pedido;
	}

	public void setPedido(Integer pedido) {
		this.pedido = pedido;
	}
}
