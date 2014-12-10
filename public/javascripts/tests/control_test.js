log("Only for test purpose, in production remove control_test.js file");



function fake_controlllers_action(){
    var shipController = new Controller();

    shipController.dispatch({
        type:"map_init_configuration",
        x_size: 1200,
        y_size: 500
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
        ],
        friendly:[
            {
                x: 200, y: 250.0 + delta, user_nick: 3
            }
        ]
    });



    var delta = 2;
    window.setInterval(function(){
        shipController.dispatch({
            type:"position",
            enemy:[
                {
                    x: 100.0 + delta, y: 150.0 + delta, user_nick: 1
                },
                {
                    x: 140.0 + delta, y: 250.0 + delta, user_nick: 2
                }
            ],
            friendly:[
                {
                    x: 200, y: 250.0 + delta, user_nick: 3
                }
            ]
        });
        delta += 2;
    }, 1000);


    // get message to server
    window.setInterval(function(){
        log("NAVIGATION_MESSAGE=" + JSON.stringify(shipController.get_navigation()));
    }, 1000);


}

fake_controlllers_action();