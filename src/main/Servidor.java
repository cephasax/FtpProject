package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import model.LightFtpObject;
import model.LightFtpService;


/**
 * Servidor TCP que utiliza os servicos providos por
 * um objeto da Classe LightFtpService para troca de
 * mensagens com um cliente de forma adequada e definida
 * 
 * Cada cliente ou servidor possui um objeto da classe
 * LightFtpService e define para ele o endereco do diretorio
 * onde serao armazenados os arquivos recebidos
 * 
 * A instancia local de LightFtpService mantera o diretorio 
 * fixo escolhido para o cliente e para o servidor de forma
 * individual e armazenara a instancia atual do objeto LightFtpObject(Lfo)
 * 
 * O serviço é possivel pela troca de objetos Lfo entre cliente
 * e servidor, se forma continuada. O objeto Lfo mais recentemente
 * recebido substitui o anterior(seja no servidor ou cliente) e entao 
 * acoes sao escolhidas mediante os atributos constantes na instancia mais
 * nova do lfo. As acoes modificam essa instancia e enviam-na de volta ou
 * aguardam informações(cliente) para outras medidas.
 * 
 * @author Cephas Barreto
 *
 */
public class Servidor {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String diretorioServidor = new String("C:\\Users\\Cephas\\Documents\\teste\\servidor");
		LightFtpService lfs = new LightFtpService(diretorioServidor);

		ServerSocket connectionSocket = new ServerSocket(5555);
		Socket socket = connectionSocket.accept();
		System.out.println("Servidor de votação aguardando conexao:");
		System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado! \n");

		ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());

		while (true) {
			Object objeto = new Object();
			objeto = inObjeto.readObject();
			LightFtpObject temp = (LightFtpObject) objeto;
			lfs.setLfo(temp);

			if (lfs.getLfo().getPedido() < 4) {
				lfs.worker();
				outObjeto.writeObject(lfs.getLfo());
			} 
			else {
				System.out.println("Cliente desconectado");
				socket.close();
				socket = connectionSocket.accept();
				System.out.println("Servidor aguardando conexao:");
				System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado! \n");
				outObjeto = new ObjectOutputStream(socket.getOutputStream());
				inObjeto = new ObjectInputStream(socket.getInputStream());
			}
		}
	}
}
