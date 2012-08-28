package br.com.paniz;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public class Player implements OnTextMessage {
	private Connection connection;
	private Game game;

	public Player(Game game) {
		this.game = game;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		System.out.println("CLOSE");
//		game.remove(this);
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println("OPEN");
		game.add(this);
		this.connection = connection;
	}

	@Override
	public void onMessage(String message) {
		this.game.onMessage(message, this);
	}

	public void sendMessage(String message) {
		try {
			System.out.println(message);
			this.connection.sendMessage(message);
		} catch (IOException e) {
			this.game.remove(this);
			this.connection.close();
		}
	}
}
