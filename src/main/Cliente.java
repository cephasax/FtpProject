package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import model.LightFtpObject;
import model.LightFtpService;

/**
 * Cliente TCP que utiliza os servicos providos por
 * um objeto da Classe LightFtpService para troca de
 * mensagens com um servidor de forma adequada e definida
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
public class Cliente {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		String diretorioCliente = new String("C:\\Users\\Cephas\\Documents\\teste\\usuario");
		LightFtpService lfs = new LightFtpService(diretorioCliente);

		Socket socket = new Socket("localhost", 5555);
		System.out.println("Cliente conectado com o servidor " + socket.getInetAddress().getHostAddress() + ":"
				+ socket.getPort() + "\n");

		ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());

		String entradaUsuario = new String();
		Scanner in = new Scanner(System.in);
		Integer pedido = 0;
		System.out.println("Bem vindo ao servico FTP-Light");
		System.out.println("------------------------------\n");

		while (pedido != 4) {
			boasVindasMenu();
			entradaUsuario = in.nextLine();
			pedido = Integer.valueOf(entradaUsuario);

			if (pedido > 0 && pedido < 4) {
				if (pedido == 2 || pedido == 3) {
					opcaoCliente(pedido, lfs, in);
				}
				lfs.getLfo().setPedido(pedido);
				outObjeto.writeObject(lfs.getLfo());

				Object objeto = new Object();
				objeto = inObjeto.readObject();
				LightFtpObject temp = (LightFtpObject) objeto;
				lfs.setLfo(temp);
				lfs.worker();
			} 
			else if (pedido == 4) {
				lfs.getLfo().setPedido(pedido);
				outObjeto.writeObject(lfs.getLfo());
				socket.close();
				in.close();
				System.out.println("Cliente encerrado!");
				break;
			} 
			else {
				System.out.println("opcao invalida");
			}
		}
	}

	public static void boasVindasMenu() {
		System.out.println();
		System.out.println("Digite a opcao desejada: \n" + "1 - Receber lista de arquivos disponiveis\n"
				+ "2 - Receber arquivo\n" + "3 - Enviar arquivo\n" + "4 - sair\n");
	}

	public static void opcaoCliente(int pedido, LightFtpService lfs, Scanner scanner) throws IOException {
		if (pedido == 2) {
			System.out.println("Digite o nome do arquivo que deseja receber: ");
			lfs.getLfo().setNomeArquivo(scanner.nextLine());
		} else if (pedido == 3) {
			lfs.enviaArquivoCliente(scanner);
		}
	}
}
