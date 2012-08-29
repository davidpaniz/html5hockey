package br.com.paniz;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

@WebServlet(urlPatterns = "/sync/*")
public class HockeyServlet extends WebSocketServlet {
	private static final long serialVersionUID = 2720754532619759785L;
	private Map<String, Game> games = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String str) {
		String gameId = req.getPathInfo();
//		System.out.println(gameId);
		Player p = null;
		if (games.containsKey(gameId)) {
			Game game = games.get(gameId);
			if (!game.isFull()) {
				p = new Player(game);
			}
		} else {
			Game g = new Game();
			games.put(gameId, g);
			p = new Player(g);
		}
		return p;
	}

}
