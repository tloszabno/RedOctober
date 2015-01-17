
function SubMap(map_x_size, map_y_size, radar_radius) {
    var self = this;
    var ships_array = [];
    var torpedoes = [];
    var sprites = [];

    var drawShip = function (name, x,y,rotation, color, isMy){
        var ctx = new PIXI.Graphics();
        var text = new PIXI.Text(name, {font:"15px Arial", fill:"black"});
        text.position.x = -4;
        text.position.y = -4;
        ctx.lineStyle(3, color );

        ctx.beginFill(0x999999);
        ctx.drawCircle(0, 0, 10);
        ctx.endFill();
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

    var drawRadar = function (x, y,radar_radius){
        var radar = new PIXI.Graphics();
        radar.position.x = x;
        radar.position.y = y;
        radar.lineStyle(1, "red");
        radar.drawCircle(0, 0, radar_radius);

        return radar;
    }


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
    var torpedoSpeed = 70;
    var torpedoReloadTime = 1;
    var torpedoReleaseTime = Date.now();

    var speed = 0;
    var dalfa = 0;

    var mainShip = undefined;
    var destroyed = false;
    var radar = drawRadar(-1000, -1000, radar_radius);
    stage.addChild(radar)

    var animate = function () {
        nowTime = Date.now();
        deltatime = (nowTime - lastTime) / 1000;
        lastTime = nowTime;


        if( mainShip !== undefined && !destroyed ) {
            mainShip.rotation += dalfa * deltatime;
            mainShip.position.x += Math.sin(-mainShip.rotation) * speed * deltatime;
            mainShip.position.y += Math.cos(mainShip.rotation) * speed * deltatime;

            mainShip.position.x = (mainShip.position.x % renderer.width);
            mainShip.position.y = (mainShip.position.y % renderer.height);

            radar.position.x = mainShip.position.x
            radar.position.y = mainShip.position.y

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
            if (torpedoes[i].getExpoded == true){

            }
        }

        if (destroyed) {
            var texture = PIXI.Texture.fromImage("assets/images/explosion.png")
            var sprite = new PIXI.Sprite(texture)

            sprite.position.x = mainShip.position.x - 30;
            sprite.position.y = mainShip.position.y - 33;
            stage.addChild(sprite);
        }
        // render the stage
        requestAnimFrame(animate);
        renderer.render(stage);
    };

    requestAnimFrame(animate);

    // API
    this.getMap = function (){
        return map;
    };

    this.setTorpedoReleaseTime = function (releaseTime) {
        torpedoReleaseTime = releaseTime;
    }

    this.getTorpedoReleaseTime = function(){
        return torpedoReleaseTime;
    }

    this.setTorpedoReloadTime = function(time){
        torpedoReloadTime = time;
    }

    this.getTorpedoReloadTime = function(){
        return torpedoReloadTime;
    }

    this.setTorpedoSpeed = function(x){
        torpedoSpeed = x;
    }

    this.getTorpedoSpeed = function(){
        return torpedoSpeed;
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
        stage.addChild(ship);
    };

    this.moveShip = function (name, x,y, angle, color) {
        var ship = stateShips[name];
        drawShip(name, x, y, angle, color);
        ship.position.x = x;
        ship.position.y = y;
        ship.rotation = angle;
    };

    this.putMyShip = function(x,y,angle, color){
        var name = MY_SHIP_CONFIG.default_id;
        mainShip = drawShip(name, x,y, angle, color, true);
        stateShips[name] = mainShip;
        stage.addChild(radar);
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

    this.launch = function(){//TODO now only a test method, to add torpedos use refreshTorpedos directly
        if (((Date.now() - torpedoReleaseTime)/1000) > torpedoReloadTime ){
            torpedoReleaseTime = Date.now();

            var torpedo = new Torpedo(mainShip.position.x, mainShip.position.y, false);
            var torpedo2 = new Torpedo(mainShip.position.x + 30, mainShip.position.y + 30 , true);
            var torpedo_list = [];
            torpedo_list.push(torpedo);
            torpedo_list.push(torpedo2);

            this.refreshTorpedoes(torpedo_list);
        }
    }

    this.refreshTorpedoes = function(torpedo_list){
        this.destroyTorpedoes();
        for (i = 0; i < torpedo_list.length; i++){
            this.addTorpedo(torpedo_list[i]);
        }
    }

    this.addTorpedo = function(missle){
        var torpedo = new PIXI.Graphics();
        if(missle.getExploded() == false){
            torpedo.lineStyle(3,"black" );
            torpedo.drawCircle(0, 0, 1);
            torpedo.position.x = missle.getX();
            torpedo.position.y = missle.getY();
        }
        if (missle.getExploded() == true){
            var texture = PIXI.Texture.fromImage("assets/images/small_explosion.png");
            var sprite = new PIXI.Sprite(texture);

            //setting sprite center positions
            sprite.position.x = missle.getX() - 6;
            sprite.position.y = missle.getY() - 7;
            sprites.push(sprite);
            torpedo.addChild(sprite);
        }
        stage.addChild(torpedo);
        torpedoes.push(torpedo);
    }

    this.destroy = function(destroyerName){
        destroyed = true;
        var text = new PIXI.Text("You were destroyed by player: " + destroyerName, {font:"30px Arial", fill:"red"});
        text.position.x = 200;
        text.position.y = map_y_size/2;
        stage.addChild(text);
    }

    this.destroyTorpedoes = function(){//necessary to remove objects from map
        for (i = torpedoes.length; i--;){
            stage.removeChild(torpedoes[i]);
            torpedoes.splice(i, 1);
        }
        for (j = sprites.length; j--;){
            stage.removeChild(sprites[j]);
            sprites.splice(j, 1);
        }
    }



}






