package br.com.paniz;

public class Game {
	private final int width = 400;
	private final int height = 600;
	private final int puckSize = 5;
	private final int playerSize = 20;
	private final int timeToUpdate = 500;

	private Player player1;
	private Player player2;
	private int puckX = 0;
	private int puckY = 0;
	private long lastPuckUpdate = System.currentTimeMillis();
	private int puckXSpeed = 0;
	private int puckYSpeed = 0;

	public Game() {
		puckX = middle(width, puckSize);
		puckY = middle(height, puckSize);
		new Thread(new GameUpdater(this)).start();
	}

	private int middle(int totalSize, int elementSize) {
		return totalSize / 2 - elementSize / 2;
	}

	private int initialY(boolean isPlayer1) {
		if (isPlayer1) {
			return height - (height / 6);
		} else {
			return (height / 6);
		}
	}

	public void add(Player player) {
		System.out.println("Add");
		if (player1 == null) {
			player1 = player;
			player1.setInitialPosition(middle(width, playerSize),
					initialY(true));
		} else if (player2 == null) {
			player2 = player;
			player2.setInitialPosition(middle(width, playerSize),
					initialY(false));
		}
	}

	public void remove(Player player) {
		if (player1 == player) {
			player1 = null;
		} else if (player2 == player) {
			player2 = null;
		}
	}

	public void refresh() {
		colissionCheck(player1);
		colissionCheck(player2);

		if (this.lastPuckUpdate + this.timeToUpdate < System
				.currentTimeMillis()) {
			puckX += puckXSpeed;
			puckY += puckYSpeed;
			if (puckX > width || puckX < 0) {
				puckXSpeed *= -1;
			}
			if (puckY > height || puckY < 0) {
				puckYSpeed *= -1;
			}
		}

		String puckPosition = puckX + "," + puckY;
		if (player1 != null && player2 != null) {
			player1.sendMessage(puckPosition + "|" + player2.getCoordinate());
			player2.sendMessage(puckPosition + "|" + player1.getCoordinate());
		}
	}

	private void colissionCheck(Player player) {
		if (player == null) {
			return;
		}
		double sqrt = Math.sqrt((player.getX() - puckX)
				* (player.getX() - puckX) + (player.getY() - puckY)
				* (player.getY() - puckY));
		if (sqrt < (puckSize + playerSize)) {
			if (player.getX() < puckX) {
				puckXSpeed = 5;
			} else {
				puckXSpeed = -5;
			}
			if (player.getY() < puckY) {
				puckYSpeed = 5;
			} else {
				puckYSpeed = -5;
			}
		}
	}

	public boolean isFull() {
		return player1 != null && player2 != null;
	}
}
