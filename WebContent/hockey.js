function Circle(x, y, size, color) {
	this.x = x;
	this.y = y;
	this.size = size;
	this.color = color;
	this.draw = function(ctx) {
		ctx.beginPath();
		ctx.arc(this.x, this.y, this.size, 0, 2 * Math.PI, true);
		ctx.fillStyle = color;
		ctx.fill();
	};

};

var Size = {
	width : 400,
	height : 600,
	player : 20,
	puck : 5,

	center : function(totalSize, elementSize) {
		return totalSize / 2 - elementSize / 2;
	},

	initialY : function(isPlayer1) {
		if (isPlayer1) {
			return Size.height - (Size.height / 6);
		} else {
			return (Size.height / 6);
		}
	}
};

var Hockey = {
	width : 400,
	height : 600,
	circleSize : 20,
	puckSize : 5,
	ws : null,
	player1 : new Circle(Size.center(Size.width, Size.player), Size
			.initialY(true), Size.player, "red"),
	player2 : new Circle(Size.center(Size.width, Size.player), Size
			.initialY(false), Size.player, "blue"),
	puck : new Circle(Size.center(Size.width, Size.puck), Size.center(
			Size.height, Size.player), Size.puck, "black"),

	rink : null,
	context : null,

	clear : function() {
		Hockey.context.clearRect(0, 0, Hockey.width, Hockey.height);
	},

	start : function() {
		Hockey.rink = document.getElementById("rink");
		Hockey.context = Hockey.rink.getContext("2d");

		Hockey.rink.addEventListener('mousemove', Hockey.mouseMove, false);
		var server = location.href.replace(/https?/,'ws').replace(/game/, "sync");
		console.log(server);
		Hockey.ws = new WebSocket(server);
		var ws = Hockey.ws;

		ws.onopen = function() {
			// console.log('Conexão aberta com sucesso');
		};

		ws.onmessage = function(message) {
			newPos = message.data.split("|");
			console.log(newPos);
			puckPos = newPos[0].split(",");
			Hockey.puck.x = puckPos[0];
			Hockey.puck.y = puckPos[1];

			player2Pos = newPos[1].split(",");
			Hockey.player2.x = player2Pos[0];
			Hockey.player2.y = player2Pos[1];

			Hockey.draw();
		};

		// Hockey.draw();
	},

	mouseMove : function(evt) {
		var mousePos = Hockey.getMousePos(evt);

		Hockey.ws.send(mousePos.x + "," + mousePos.y);

		Hockey.player1.x = mousePos.x;
		Hockey.player1.y = mousePos.y;
	},

	draw : function() {
		Hockey.clear();
		// console.log(Hockey.player1);
		Hockey.player1.draw(Hockey.context);
		Hockey.player2.draw(Hockey.context);
		Hockey.puck.draw(Hockey.context);
	},
	getMousePos : function(evt) {
		var rect = Hockey.rink.getBoundingClientRect(), root = document.documentElement;

		// return relative mouse position
		var mouseX = evt.clientX - rect.top - root.scrollTop;
		var mouseY = evt.clientY - rect.left - root.scrollLeft;
		return {
			x : mouseX,
			y : mouseY
		};
	}
}

window.onload = function() {
	Hockey.start();
};
