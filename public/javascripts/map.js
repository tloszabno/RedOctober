
function SubMap(map_x_size, map_y_size) {
    var self = this;
    var ships_array = [];
    var torpedoes = []

    var drawShip = function (name, x,y,rotation, color, isMy){
        var ctx = new PIXI.Graphics();
        var text = new PIXI.Text(name, {font:"15px Arial", fill:"black"});
        text.position.x = -4;
        text.position.y = -4;
        ctx.lineStyle(3, color );

        ctx.beginFill(0x999999);
        ctx.drawCircle(0, 0, 10);
        ctx.endFill();
        ctx.lineStyle(1, color)
        ctx.drawCircle(0, 0, 100)
        ctx.moveTo(0, 10);

        if( isMy == true) {
            ctx.lineTo(0, 18);
        }

        ctx.position.x = x;
        ctx.position.y = y;
        ctx.rotation = rotation;
        ctx.name = name;
        ctx.addChild(text);
        ctx.color = color;

        ships_array.push(ctx);

        return ctx;
    };


// main view of PIXI Canvas
    var stage = new PIXI.Stage(0x66CCFF);
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
    var torpedoSpeed = 70
    var torpedoReloadTime = 1
    var torpedoReleaseTime = Date.now();

    var speed = 0;
    var dalfa = 0;

    var mainShip = undefined;


    var animate = function () {
        nowTime = Date.now();
        deltatime = (nowTime - lastTime) / 1000;
        lastTime = nowTime;


        if( mainShip !== undefined ) {
            mainShip.rotation += dalfa * deltatime;
            mainShip.position.x += Math.sin(-mainShip.rotation) * speed * deltatime;
            mainShip.position.y += Math.cos(mainShip.rotation) * speed * deltatime;

            mainShip.position.x = (mainShip.position.x % renderer.width);
            mainShip.position.y = (mainShip.position.y % renderer.height);

            // handle situations when ships whant to move outside borders
            if (mainShip.position.x < 0) {
                mainShip.position.x += 1;//renderer.width;
                self.setSpeed(0);
            }
            if (mainShip.position.y < 0 ) {
                mainShip.position.y += 1;//renderer.height;
                self.setSpeed(0);
            }
            // 10 is ship diameter
            if (mainShip.position.x + 10 >= map_x_size) {
                mainShip.position.x -= 1;//renderer.width;
                self.setSpeed(0);
            }
            if (mainShip.position.y + 10 >= map_y_size ) {
                mainShip.position.y -= 1;//renderer.height;
                self.setSpeed(0);
            }

        }
        for (i = torpedoes.length; i--;){
            if (torpedoes[i][1] > 0 ){

                var rot = torpedoes[i][0].rotation;

                torpedoes[i][1] -= Math.abs(Math.sin(-rot) * torpedoes[i][2] * deltatime)+Math.abs(Math.cos(rot) * torpedoes[i][2] * deltatime)
                torpedoes[i][0].position.x += Math.sin(-rot) * torpedoes[i][2] * deltatime;
                torpedoes[i][0].position.y += Math.cos(rot) * torpedoes[i][2] * deltatime;



            }else if (torpedoes[i][1] < 0){
                torpedoes[i][0].position.x = -10
                torpedoes[i][0].position.y = -10
                stage.removeChild(torpedoes[i][0])
                torpedoes.splice(i, 1)
            }

        }

        requestAnimFrame(animate);
        // render the stage
        renderer.render(stage);

    };

    requestAnimFrame(animate);

    // API
    this.getMap = function (){
        return map;
    };

    this.setTorpedoReleaseTime = function (releaseTime) {
        torpedoReleaseTime = releaseTime
    }

    this.getTorpedoReleaseTime = function(){
        return torpedoReleaseTime
    }

    this.setTorpedoReloadTime = function(time){
        torpedoReloadTime = time
    }

    this.getTorpedoReloadTime = function(){
        return torpedoReloadTime
    }

    this.setTorpedoSpeed = function(x){
        torpedoSpeed = x
    }

    this.getTorpedoSpeed = function(){
        return torpedoSpeed
    }

    this.setSpeed = function (v) {
        speed = v;
    };
    this.setRotationSpeed = function (rotationSpeed) {
        dalfa = rotationSpeed;
    };

    this.getXPosition = function () {
        return mainShip.position.x;
    };

    this.getYPosition = function () {
        return mainShip.position.y;
    };

    this.getRotation = function () {
        return mainShip.rotation;
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
        drawShip(name, x, y, angle, color);
        ship.position.x = x;
        ship.position.y = y;
        ship.rotation = angle
    };

    this.putMyShip = function(x,y,angle, color){
        var name = MY_SHIP_CONFIG.default_id;
        mainShip = drawShip(name, x,y, angle, color, true);
        stateShips[name] = mainShip;
        stage.addChild(mainShip);
    };

    this.putOrMoveMainShip = function(x,y,angle, color){
        if( mainShip === undefined){
            this.putMyShip(x,y,angle, color);
        }else{
            this.moveShip(MY_SHIP_CONFIG.default_id,x,y,angle,color);
        }
    };

    this.addOrMoveShip = function(name, x, y, angle, color){
        var ship = stateShips[name];
        if (ship === undefined) {
            this.addShip(name, x, y, angle, color);
        } else {
            this.moveShip(name, x, y, angle, color);
        }
    };


    this.removeAllShipsWithoutMine = function(){
        var my_ship_name = MY_SHIP_CONFIG.default_id;
        ships_array.slice(0).forEach(function(el){
            if( el.name !== my_ship_name ){
                stage.removeChild(el);
                // not efective and goot but has one benefit: it works :)

                // FIXME: feel free to refator
                var idx = ships_array.indexOf(el);
                ships_array = ships_array.slice(0,idx).concat(ships_array.slice(idx+1, 0));

                stateShips[el.name] = undefined;
            }
        });
    };

    this.launch = function(){
        if (((Date.now() - torpedoReleaseTime)/1000) > torpedoReloadTime ){
            torpedoReleaseTime = Date.now();
            var rot = mainShip.rotation
            this.addTorpedo(mainShip.position.x + Math.sin(-rot)*20, mainShip.position.y + Math.cos(rot)*20, rot)
        }
    }

    this.addTorpedo = function(x, y, rot){

        var torpedo = new PIXI.Graphics();
        torpedo.lineStyle(3,"black" );
        torpedo.drawCircle(0, 0, 1);
        torpedo.position.x = x
        torpedo.position.y = y
        torpedo.rotation = rot
        torpedoRange = 1000;
        torpedoes.push([torpedo, torpedoRange, torpedoSpeed])

        stage.addChild(torpedo)
    }
}






