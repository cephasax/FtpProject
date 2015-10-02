package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 
 * @author Cephas Barreto
 *
 *	Classe que oferece uma serie de metodos publicos e privados para a troca de objetos
 *	do tipo LightFtpObject de forma correta e simples.
 *
 *	Alem de propor os metodos para a manipulacao das informacoes essa classe tambem tem
 *	como objetivo deixar a interacao entre servidor e cliente mais limpa em sua implementacao
 *	na medida em que procura cuidar dos tipos de pedidos que foram enviados e executar os
 *	respectivos metodos
 *
 *	A interacao baseia-se unicamente em um objeto definido de forma completa(LightObjectObject) e
 *	a troca, apenas desse objeto, entre o servidor e o cliente. Ambos possuem um objeto do tipo
 *	LightFtpService, o qual possui um único LightFtpObject. Como esse objeto carrega toda a informa
 *	cao de comunicacao, quando o servidor ou cliente o recebem, entao e iniciado o seguinte processo:
 *		1 - substituicao da instancia atual do LightFtpObject pela recebida 
 *		2 - chamada do metodo worker() para essa "nova" carga de informacao 
 *			2.1 - realizacao de tarefas pertinentes
 *			2.2 - carga das informacoes alteradas
 *			2.3 - devolucao do objeto para o cliente ou recebimento de informacoes do cliente para 
 *				novas alteracoes
 */

public class LightFtpService {

	private LightFtpObject lfo;
	private String diretorio;
	
	/*
	 * Construtor - instancia a classe para um diretorio especifico
	 * de forma a manter as informacoes e acoes indicadas pelo objeto
	 * LightFtpObject em um local fixo, escolhido pelo desenvolvedor
	 */
	public LightFtpService(String diretorio) {
		this.diretorio = new String(diretorio);
		this.lfo = new LightFtpObject();
	}
	
	/*
	 * Metodo que faz a escolhe de qual metodo privado chamar
	 * a partir do atributo pedido, pertencente ao objeto lfo,
	 * que por sua vez pertence a instancia local da classe LightFtpService
	 */
 	public void worker() throws IOException, ClassNotFoundException{
 		int pedido = this.lfo.getPedido();
 		
 		if(pedido == 0){
			mostraListaArquivos();
		}
		else if(pedido == 1){
			enviaListaArquivos();
		}
		else if(pedido == 2){
			enviaArquivo();
		}
		else if(pedido == 3){
			recebeArquivo();
		}
 	}
	
 	/*
 	 * Metodos alteradores privados(chamados apenas atraves do metodo worker)
 	 */
 	
 	private void enviaListaArquivos() throws IOException { 
		this.lfo.criarNomesArquivos(this.diretorio);
		this.lfo.setPedido(0);
	}
 	
	private void mostraListaArquivos(){
		System.out.println();
		System.out.println("Arquivos disponiveis no diretorio do servidor:");
		for(String string: this.lfo.getArquivos()){
			System.out.println(string);
		}
	}
		
	private void recebeArquivo() throws ClassNotFoundException, IOException {
		
		FileOutputStream fso = new FileOutputStream(this.diretorio + "\\" + this.lfo.getNomeArquivo());
		fso.write(this.lfo.getArquivo());
		fso.close();
		
		System.out.println("Arquivo: " + this.lfo.getNomeArquivo() 
							+ " salvo com sucesso");
	}

	private void enviaArquivo() throws ClassNotFoundException, IOException {
		String nome = this.lfo.getNomeArquivo();
		String diretorio = this.diretorio;
		
		Path path = Paths.get(diretorio + "\\" + nome);
		byte[] data = Files.readAllBytes(path);
		
		this.lfo.setArquivo(data);
		this.lfo.setPedido(3);
	}
	
	/*
 	 * Metodo alterador public(necessarios para solicitar informacoes
 	 * adicionais do usuario quando se quer enviar um arquivo para o servidor)
 	 */
	public void enviaArquivoCliente(Scanner input) throws IOException{
		LightFtpObject temp = new LightFtpObject();
		System.out.println("Digite o caminho do diretorio do seu arquivo:");
		String diretorioTemp = new String(input.nextLine());
		
		temp.criarNomesArquivos(diretorioTemp);
		mostraListaArquivosCliente(temp);
				
		System.out.println("Digite o nome do arquivo que deseja enviar:");
		String nomeTemp = new String(input.nextLine());
		
		
		Path path = Paths.get(diretorioTemp + "\\" + nomeTemp);
		byte[] data = Files.readAllBytes(path);
		
		this.lfo.setNomeArquivo(nomeTemp);
		this.lfo.setArquivo(data);
		this.lfo.setPedido(3);
		
		
	}
	
	private void mostraListaArquivosCliente(LightFtpObject lfo){
		System.out.println();
		System.out.println("Arquivos disponiveis para envio:");
		for(String string: lfo.getArquivos()){
			System.out.println(string);
		}
	}

	//GETTERS e SETTERS
	public LightFtpObject getLfo() {
		return lfo;
	}

	public void setLfo(LightFtpObject lfo) {
		this.lfo = lfo;
	}


	
	public String getDiretorio() {
		return diretorio;
	}


	
	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}
	
}
