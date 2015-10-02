package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import model.LightFtpObject;
import model.LightFtpService;

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
