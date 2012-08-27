package br.com.paniz;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

@WebServlet(urlPatterns="/sincronizar")
public class HockeyServlet extends WebSocketServlet {
	 private final Set<SyncWebSocket> usuarios = new CopyOnWriteArraySet<SyncWebSocket>();
	 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String str) {
		return new SyncWebSocket(usuarios);
	}

}
