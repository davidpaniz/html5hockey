package br.com.paniz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

@WebServlet(urlPatterns = "/sincronizar")
public class HockeyServlet extends WebSocketServlet {
	private static final long serialVersionUID = 2720754532619759785L;
	private List<Game> games = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String str) {
		Player p = null;
		for (int i = 0; i < games.size(); i++) {
			Game game = games.get(i);
			if (!game.isFull()) {
				p = new Player(game);
				break;
			}
		}

		if (p == null) {
			Game g = new Game();
			games.add(g);
			p = new Player(g);
		}

		return p;
	}

}
