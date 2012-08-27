package br.com.paniz;

import java.io.IOException;
import java.util.Set;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public class SyncWebSocket implements OnTextMessage {

	private Set<SyncWebSocket> usuarios;
	private Connection connection;

	public SyncWebSocket(Set<SyncWebSocket> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		usuarios.remove(this);
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println("OPEN");
		usuarios.add(this);
		this.connection = connection;
	}

	@Override
	public void onMessage(String message) {
		for (SyncWebSocket usuario : usuarios) {
			if (usuario != this) {
				try {
					usuario.connection.sendMessage(message);
				} catch (IOException e) {
					usuarios.remove(usuario);
					usuario.connection.close();
				}
			}
		}
	}

}
