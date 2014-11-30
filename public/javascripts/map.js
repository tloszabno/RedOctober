// create an new instance of a pixi stage
var stage = new PIXI.Stage(0x66FF99);

// create a renderer instance
var renderer = PIXI.autoDetectRenderer(900, 600);

// add the renderer view element to the DOM
var map = renderer.view

var deltatime = 0
var lastTime = Date.now()
var nowTime = Date.now()
requestAnimFrame(animate);

var ctx = new PIXI.Graphics();

ctx.lineStyle(3, 0xFF0000);

var speed = 4
var dalfa = 0

ctx.beginFill(0x999999);
ctx.drawCircle(0,0,10);
ctx.moveTo(0, 10);
ctx.lineTo(0, 18);

stage.addChild(ctx);

function animate() {

    nowTime = Date.now()

    deltatime = (nowTime - lastTime) / 1000 

    lastTime = nowTime
    requestAnimFrame(animate);

    ctx.rotation += dalfa * deltatime;
    ctx.position.x += Math.sin(-ctx.rotation) * speed * deltatime;
    ctx.position.y += Math.cos(ctx.rotation) * speed * deltatime;

    ctx.position.x = (ctx.position.x % renderer.width);
    ctx.position.y = (ctx.position.y % renderer.height);

    if (ctx.position.x < 0) {
        ctx.position.x += renderer.width
    }
    if (ctx.position.y < 0) {
        ctx.position.y += renderer.height
    }

    // render the stage
    renderer.render(stage);
}

function setSpeed(v) {
    speed = v
}

function setRotationSpeed(av){
    dalfa = av
}

function getXPosition() {
    return ctx.position.x
}

function getYPosition() {
    return ctx.position.y
}

function getRotation() {
    return ctx.rotation
}


