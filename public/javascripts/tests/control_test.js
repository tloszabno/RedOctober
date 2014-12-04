log("Only for test purpose, in production remove control_test.js file");

function assertTrue(condition){
    if(!condition){
        throw "Assertion failed";
    }
}


function fake_controlllers_action(){
    var shipController = new Controller();

    shipController.dispatch({
        type:"map_init_configuration",
        x_size: 1200,
        y_size: 400
    });

    shipController.dispatch({
        type:"position",
        my:{
            x: 10.0,y: 11.0
        },
        enemy:[
            {
                x: 100.0, y: 150.0, user_nick: 1
            },
            {
                x: 140.0, y: 250.0, user_nick: 2
            }
        ]
    });



}




fake_controlllers_action();