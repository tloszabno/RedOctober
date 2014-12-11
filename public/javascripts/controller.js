
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
        log("[Entry] get_navigation");

        var current_x = map.getXPosition();
        var current_y = map.getYPosition();

        var dx = current_x - position_cache[my_ship_name].previous_x;
        var dy = current_y - position_cache[my_ship_name].previous_y;

        log("Current x=" + current_x + "Current y=" + current_y + " dx=" + dx + " dy=" + dy)

        refresh_position_cache(my_ship_name, current_x, current_y);

        var current_velocity = map.getSpeed();

        // TODO: get user nick from some hidden input
        var user_nick = "name";

        var message = {
            type: "navigation",
            user_nick: user_nick,
            current_x: current_x,
            current_y: current_y,
            x_prim: dx,
            y_prim: dy
        };

        log("[Exit] get_navigation");
        return message;
    };

    // ********
    // PRIVATE
    // ********

    function handle_init_map(commandObject) {
        log("[Entry] handle_init_map");

        var map_x = commandObject.x_size;
        var map_y = commandObject.y_size;
        log(" got map_x=" + map_x + " map_y=" + map_y);

        map = new SubMap(map_x, map_y);
        put_map_to_html();

        log("[Exit] handle_init_map");
    }

    function add_or_move_ships(ships, ship_type) {
        if (ships) {
            for (var i = 0; i < ships.length; ++i) {
                var ship = ships[i];
                log("Putting ship to [" + ship.x + "," + ship.y + "] user_nick=" + ship.user_nick + " ship_type=" + ship_type);

                // TODO: move angle to config or server - to consideration
                var angel = compute_angel(ship.user_nick, ship.x, ship.y);
                map.addOrMoveShip(ship.user_nick, ship.x, ship.y, angel, ship_type == SHIP_TYPE.Enemy ? SHIP_COLOR_OF.enemy : SHIP_COLOR_OF.friendly);

                refresh_position_cache( ship.user_nick, ship.x, ship.y );
            }
        }
    }

    var set_position_request_invocations = 0;
    function handle_set_positions_request(commandObject, sendToServerFunction) {
        log("[Entry] handle_set_positions_request");

        if(set_position_request_invocations > 0 && sendToServerFunction !== undefined){
            var msg = self.get_navigation();
        }

        set_ships_positions(commandObject);

        if( msg !== undefined ) {
            sendToServerFunction(msg);
        }


        set_position_request_invocations++;
        log("[Exit] handle_set_positions_request");
    }

    function set_ships_positions(commandObject){
        var my_ship = commandObject.my;
        if( my_ship !== undefined ) {
            log("Putting mine ship to [" + my_ship.x + "," + my_ship.y + "]");
            // TODO: move angle to config or server - to consideration

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

        switch (key) {
            case ARROW_KEYS.left:
                map.setRotationSpeed(-0.7);
                break;

            case ARROW_KEYS.up:
                if( map.getSpeed() < 0 ){
                    map.setSpeed(0);
                }
                else{
                    map.setSpeed(50);
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
                    map.setSpeed(-50);
                }
                break;

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
