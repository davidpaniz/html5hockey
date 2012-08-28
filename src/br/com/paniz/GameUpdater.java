package br.com.paniz;

public class GameUpdater implements Runnable {
	private final Game game;

	public GameUpdater(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		while (true) {
			this.game.refresh();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
