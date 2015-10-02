package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import model.LightFtpObject;
import model.LightFtpService;

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
