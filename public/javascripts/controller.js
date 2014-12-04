/**
* Here put integration things such as parsing requests from and to controller
 **/
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
    var map;

    /* INVOKE THIS FUNCTION ON MESSAGE FROM SERVER */
    this.dispatch =  function(commandObject) {
        var type = commandObject.type;
        switch (type) {
            case "position":
                handle_set_positions(commandObject);
                break;
            case "map_init_configuration":
                handle_init_map(commandObject);
                break;
            default :
                error("Got message with unknown type=" + type);
        }
    }


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
                var enemy_ship = ships[i];
                log("Putting ship to [" + enemy_ship.x + "," + enemy_ship.y + "] user_nick=" + enemy_ship.user_nick + " ship_type=" + ship_type);

                // TODO: move angle to config or server - to consideration
                map.addOrMoveShip(enemy_ship.user_nick, enemy_ship.x, enemy_ship.y, 0.2, ship_type == SHIP_TYPE.Enemy ? SHIP_COLOR_OF.enemy : SHIP_COLOR_OF.friendly);
            }
        }
    }

    function handle_set_positions(commandObject) {
        log("[Entry] handle_set_positions");

        var my_ship = commandObject.my;
        if( my_ship !== undefined ) {
            log("Putting mine ship to [" + my_ship.x + "," + my_ship.y + "]");
            // TODO: move angle to config or server - to consideration
            map.putOrMoveMainShip(my_ship.x, my_ship.y, 0.2, SHIP_COLOR_OF.mine);
        }

        var enemy_ships = commandObject.enemy;
        add_or_move_ships(enemy_ships, SHIP_TYPE.Enemy);

        var friendly_ships = commandObject.friendly;
        add_or_move_ships(friendly_ships, SHIP_TYPE.Friendly);

        log("[Exit] handle_set_positions");
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


}
