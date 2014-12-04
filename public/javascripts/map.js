
function SubMap(map_x_size, map_y_size) {

    var drawShip = function (name, x,y,rotation, color){
        var ctx = new PIXI.Graphics();
        var text = new PIXI.Text(name, {font:"15px Arial", fill:"black"});
        text.position.x = -4;
        text.position.y = -4;
        ctx.lineStyle(3, color );
        ctx.beginFill(0x999999);
        ctx.drawCircle(0, 0, 10);
        ctx.moveTo(0, 10);
        ctx.lineTo(0, 18);
        ctx.position.x = x;
        ctx.position.y = y;
        ctx.rotation = rotation;
        ctx.name = name;
        ctx.addChild(text);
        ctx.color = color;
        return ctx;
    };

// main view of PIXI Canvas
    var stage = new PIXI.Stage(0x66FF99);
// create a renderer instance
    var renderer = PIXI.autoDetectRenderer(map_x_size,  map_y_size);

// add the renderer view element to the DOM
    var map = renderer.view;

// map (dictionary) with other ships
    var stateShips = Object(); // or var map = {};
// for counting time between frames
    var deltatime = 0;
    var lastTime = Date.now();
    var nowTime = Date.now();

    var speed = 0;
    var dalfa = 0;

    var mainShip = undefined;

    var animate = function () {
        nowTime = Date.now();
        deltatime = (nowTime - lastTime) / 1000;
        lastTime = nowTime

        if( mainShip !== undefined ) {
            mainShip.rotation += dalfa * deltatime;
            mainShip.position.x += Math.sin(-mainShip.rotation) * speed * deltatime;
            mainShip.position.y += Math.cos(mainShip.rotation) * speed * deltatime;

            mainShip.position.x = (mainShip.position.x % renderer.width);
            mainShip.position.y = (mainShip.position.y % renderer.height);

            if (mainShip.position.x < 0) {
                mainShip.position.x += renderer.width
            }
            if (mainShip.position.y < 0) {
                mainShip.position.y += renderer.height
            }
        }
        requestAnimFrame(animate);
        // render the stage
        renderer.render(stage);
    };
    requestAnimFrame(animate);



    // API
    this.getMap = function (){
        return map
    };

    this.setSpeed = function (v) {
        speed = v
    };
    this.setRotationSpeed = function (rotationSpeed) {
        dalfa = rotationSpeed
    };

    this.getXPosition = function () {
        return mainShip.position.x
    };

    this.getYPosition = function () {
        return mainShip.position.y
    };

    this.getRotation = function () {
        return mainShip.rotation
    };

    this.getSpeed = function(){
        return speed;
    };

    this.addShip = function(name, x, y , angle, color) {
        var ship = drawShip(name, x, y, angle, color);
        stateShips[name] = ship;
        stage.addChild(ship)
    };

    this.moveShip = function (name, x,y, angle, color) {
        var ship = stateShips[name];
        log("ship=" + ship + " for name=" + name);
        drawShip(name, x, y, angle, color);
        ship.position.x = x;
        ship.position.y = y;
        ship.rotation = angle
    };

    this.putMyShip = function(x,y,angle, color){
        var name = MY_SHIP_CONFIG.default_id;
        mainShip = drawShip(name, x,y, angle, color);
        stateShips[name] = mainShip;
        stage.addChild(mainShip);
    }

    this.putOrMoveMainShip = function(x,y,angle, color){
        if( mainShip === undefined){
            this.putMyShip(x,y,angle, color);
        }else{
            this.moveShip(MY_SHIP_CONFIG.default_id,x,y,angle,color);
        }
    }


    this.addOrMoveShip = function(name, x, y, angle, color){
        var ship = stateShips[name];
        if (ship === undefined) {
            this.addShip(name, x, y, angle, color);
        } else {
            this.moveShip(name, x, y, angle, color);
        }
    }
}






