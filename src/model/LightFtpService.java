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
 * @author Cephas
 *
 *         PEDIDOS 0 - receber lista de arquivos 1 - enviar lista de arquivos 2
 *         - receber arquivo 3 - enviar arquivo
 */

public class LightFtpService {

	private LightFtpObject lfo;
	private String diretorio;
	
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
	
	public LightFtpService(String diretorio) {
		this.diretorio = new String(diretorio);
		this.lfo = new LightFtpObject(this.diretorio);
	}

	public LightFtpObject getLfo() {
		return lfo;
	}

	public void setLfo(LightFtpObject lfo) {
		this.lfo = lfo;
	}
	
}
