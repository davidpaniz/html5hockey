function Circle(x, y, color) {
    this.x = x;
    this.y = y;
    this.color = color;
};

var Size = {
	width: 400,
	height: 600,
	player: 20,
	puck: 5,
	
	center: function(totalSize, elementSize){
		return totalSize / 2 - elementSize / 2;
	},
	
	initialY: function(isPlayer1){
		if(isPlayer1){
			return Size.height - (Size.height / 6) ;
		}else{
			return (Size.height / 6) ;
		}
	}
};

var Hockey = {
    width: 400,
    height: 600,
    circleSize: 20,
    puckSize: 5,
    ws: null,
    player1: new Circle(Size.center(Size.width, Size.player), Size.initialY(true), "red"),
    player2: new Circle(Size.center(Size.width, Size.player), Size.initialY(false), "blue"),
    puck: new Circle(Size.center(Size.width, Size.puck), Size.center(Size.height, Size.player), "black"),

    rink:  null,
    context: null,

    clear: function() {
        Hockey.context.clearRect(0, 0, Hockey.width, Hockey.height);
    },

    start: function (){
        Hockey.rink = document.getElementById("rink");
        Hockey.context = Hockey.rink.getContext("2d");

        Hockey.rink.addEventListener('mousemove', Hockey.mouseMove, false);

        Hockey.ws = new WebSocket('ws://localhost:8080/Hockey/sincronizar');
        var ws = Hockey.ws;

        ws.onopen = function() {
            //console.log('Conexão aberta com sucesso');
        };

        ws.onmessage = function(message) {
            newPos = message.data.split(",");
            //console.log(newPos)
            Hockey.player2.x = newPos[0];
            Hockey.player2.y = Hockey.height - newPos[1];
        };

        Hockey.draw();
    },
    
    mouseMove: function(evt) {
        var mousePos = Hockey.getMousePos(evt);

        Hockey.ws.send(mousePos.x+","+ mousePos.y);

        Hockey.player1.x = mousePos.x;
        Hockey.player1.y = mousePos.y;
    },

    drawCircle: function(x, y, size, color){
        Hockey.context.beginPath();
        Hockey.context.arc(x, y, size, 0, 2 * Math.PI, true);
        Hockey.context.fillStyle = color;
        Hockey.context.fill();
    },

    draw: function() {
        Hockey.clear();
        //console.log(Hockey.player1);
        Hockey.drawCircle(Hockey.player1.x, Hockey.player1.y, Hockey.circleSize, Hockey.player1.color);
        Hockey.drawCircle(Hockey.player2.x, Hockey.player2.y, Hockey.circleSize, Hockey.player2.color);

        Hockey.drawCircle(Hockey.puck.x, Hockey.puck.y, Hockey.puckSize, Hockey.puck.color);

        setTimeout(Hockey.draw, 50);
    },
    getMousePos: function(evt) {
        var rect = Hockey.rink.getBoundingClientRect(), root = document.documentElement;

        // return relative mouse position
        var mouseX = evt.clientX - rect.top - root.scrollTop;
        var mouseY = evt.clientY - rect.left - root.scrollLeft;
        return {
            x: mouseX,
                y: mouseY
        };
    }
}

window.onload = function() {
    Hockey.start();
};
