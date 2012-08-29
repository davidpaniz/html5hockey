package br.com.paniz;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public class Player implements OnTextMessage {
	private Connection connection;
	private Game game;
	private int x;
	private int y;

	public Player(Game game) {
		this.game = game;
	}

	@Override
	public void onClose(int arg0, String arg1) {
		// System.out.println("CLOSE");
		// game.remove(this);
	}

	@Override
	public void onOpen(Connection connection) {
		// System.out.println("OPEN");
		game.add(this);
		this.connection = connection;
	}

	@Override
	public void onMessage(String message) {
		String[] split = message.split(",");
		this.x = Integer.parseInt(split[0]);
		this.y = Integer.parseInt(split[1]);
	}

	public void sendMessage(String message) {
		try {
//			System.out.println(message);
			this.connection.sendMessage(message);
		} catch (IOException e) {
			this.game.remove(this);
			this.connection.close();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getCoordinate() {
		return this.x + "," + this.y;
	}

	public void setInitialPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
