
var isDebug = true;

function log(msg){
    if(isDebug){
        var time = Date.now().toLocaleString();
        console.log("[LOG][" + time + "]~>" + msg);
    }
}
function error(msg){
    console.error(msg);
}


function Controller() {
    // fileds
    var map;
    var my_ship_name;
    var position_cache = {};
    var self = this;


    /**
     *  INVOKE THIS FUNCTION ON MESSAGE FROM SERVER
     **/
    this.dispatch =  function(commandObject, sendToServerFunction) {
        var type = commandObject.type;
        switch (type) {
            case "position":
                handle_set_positions_request(commandObject,sendToServerFunction);
                break;
            case "map_init_configuration":
                handle_init_map(commandObject);
                break;
            default :
                error("Got message with unknown type=" + type);
        }
    };

    /**
     *  INVOKE THIS FUNCTION WHEN NEED TO GET NAVIGATION MESSAGE TO SERVER
     **/
    this.get_navigation = function(){
        var current_x = map.getXPosition();
        var current_y = map.getYPosition();

        var dx = current_x - position_cache[my_ship_name].previous_x;
        var dy = current_y - position_cache[my_ship_name].previous_y;

        refresh_position_cache(my_ship_name, current_x, current_y);


        var user_nick = $("#userName").val();

        var message = {
            type: "navigation",
            user_nick: user_nick,
            current_x: current_x,
            current_y: current_y,
            x_prim: dx,
            y_prim: dy
        };

        return message;
    };

    // ********
    // PRIVATE
    // ********

    function handle_init_map(commandObject) {
        self.map_x = commandObject.x_size;
        self.map_y = commandObject.y_size;
        radar_radius = 100 // TODO

        map = new SubMap(self.map_x, self.map_y, radar_radius);
        put_map_to_html();
    }

    function add_or_move_ships(ships, ship_type) {
        if (ships) {
            for (var i = 0; i < ships.length; ++i) {
                var ship = ships[i];
                log("Putting ship to [" + ship.x + "," + ship.y + "] user_nick=" + ship.user_nick + " ship_type=" + ship_type);

                var angel = compute_angel(ship.user_nick, ship.x, ship.y);
                map.addOrMoveShip(ship.user_nick, ship.x, ship.y, angel, ship_type == SHIP_TYPE.Enemy ? SHIP_COLOR_OF.enemy : SHIP_COLOR_OF.friendly);

                refresh_position_cache( ship.user_nick, ship.x, ship.y );
            }
        }
    }

    var set_position_request_invocations = 0;
    function handle_set_positions_request(commandObject, sendToServerFunction) {

        if(set_position_request_invocations > 0 && sendToServerFunction !== undefined){
            var msg = self.get_navigation();
        }
        set_ships_positions(commandObject);

        if( msg !== undefined ) {
            sendToServerFunction(msg);
        }

        set_position_request_invocations++;
    }

    function set_ships_positions(commandObject){

        if( set_position_request_invocations > 0 ) {
            map.removeAllShipsWithoutMine();
        }

        var my_ship = commandObject.my;
        if( my_ship !== undefined && set_position_request_invocations < 1) {
            log("Putting mine ship to [" + my_ship.x + "," + my_ship.y + "]");
            my_ship_name = MY_SHIP_CONFIG.default_id;

            var angel = compute_angel(my_ship_name, my_ship.x, my_ship.y);
            map.putOrMoveMainShip(my_ship.x, my_ship.y, angel, SHIP_COLOR_OF.mine);

            refresh_position_cache(my_ship_name, my_ship.x, my_ship.y);
        }

        var enemy_ships = commandObject.enemy;
        add_or_move_ships(enemy_ships, SHIP_TYPE.Enemy);

        var friendly_ships = commandObject.friendly;
        add_or_move_ships(friendly_ships, SHIP_TYPE.Friendly);
    }

    function put_map_to_html(){
        document.body.appendChild ( map.getMap() ) ;
        document.onkeydown = checkDown;
        document.onkeyup = checkUp;
    }

    function checkDown(e) {
        var ev = e || window.event;
        var key = ev.keyCode;


        var default_speed  = MY_SHIP_CONFIG.default_speed;

        switch (key) {
            case ARROW_KEYS.left:
                map.setRotationSpeed(-0.7);
                break;

            case ARROW_KEYS.up:
                if( map.getSpeed() < 0 ){
                    map.setSpeed(0);
                }
                else{
                    map.setSpeed(default_speed);
                }
                break;

            case ARROW_KEYS.right:
                map.setRotationSpeed(0.7);
                break;

            case ARROW_KEYS.down:
                if ( map.getSpeed() > 0 ){
                    map.setSpeed(0);
                }else{
                    // supose it's french ship, it can go reverse :)
                    map.setSpeed(-default_speed);
                }
                break;

            case 17:
                map.launch();
                break

            default :
                return;
        }

    }

    function checkUp(e) {
        var ev = e || window.event;
        var key = ev.keyCode;

        switch (key) {
            case ARROW_KEYS.left:
                map.setRotationSpeed(0.0);
                break;

            case ARROW_KEYS.up:
                break;

            case ARROW_KEYS.right:
                map.setRotationSpeed(0.0);
                break;

            case ARROW_KEYS.down:
                break;

            case 17:
                map.launch();
                break

            case 68:
                map.destroy("Autodestruction");
                break;

            default :
                return;
        }
    }

    function refresh_position_cache(ship_name, x, y){
        position_cache[ship_name] = {
            previous_x: x,
            previous_y: y
        };
    }
    function compute_angel(ship_name, new_x, new_y){

        if( position_cache[ship_name] !== undefined ) {
            var previous_x = position_cache[ship_name].previous_x;
            var previous_y = position_cache[ship_name].previous_y;
        }

        var dx = previous_x - new_x;
        var dy = previous_y - new_y ;

        if( previous_x === undefined || previous_y === undefined){
            return MY_SHIP_CONFIG.default_angel;
        }

        var angel = Math.atan2(dx, -dy);

        return angel;
    }





}
