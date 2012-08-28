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
	}

	private int middle(int totalSize, int elementSize) {
		return totalSize / 2 - elementSize / 2;
	}

	// private int initialY(boolean isPlayer1) {
	// if (isPlayer1) {
	// return height - (height / 6);
	// } else {
	// return (height / 6);
	// }
	// }

	public void add(Player player) {
		if (player1 == null) {
			player1 = player;
		} else if (player2 == null) {
			player2 = player;
		}
	}

	public void remove(Player player) {
		if (player1 == player) {
			player1 = null;
		} else if (player2 == player) {
			player2 = null;
		}
	}

	public void onMessage(String message, Player player) {
		String[] split = message.split(",");
		int playerX = Integer.parseInt(split[0]);
		int playerY = Integer.parseInt(split[1]);
		double sqrt = Math.sqrt((playerX - puckX) * (playerX - puckX)
				+ (playerY - puckY) * (playerY - puckY));
		if (sqrt < (puckSize + playerSize)) {
			if (playerX < puckX) {
				puckXSpeed = 5;
			} else {
				puckXSpeed = -5;
			}
			if (playerY < puckY) {
				puckYSpeed = 5;
			} else {
				puckYSpeed = -5;
			}
		}
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
		player.sendMessage(puckPosition);

		if (player1 == player && player2 != null) {
			player2.sendMessage(puckPosition + "|" + playerX + "," + playerY);
		} else if (player2 == player && player1 != null) {
			player1.sendMessage(puckPosition + "|" + playerX + "," + playerY);
		}
	}

	public boolean isFull() {
		return player1 != null && player2 != null;
	}
}
