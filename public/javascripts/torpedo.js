    function Torpedo(position_x, position_y, exploded){
        var x = position_x;
        var y = position_y;
        var dx;
        var dy;

        var exploded = exploded;

        this.setExploded = function(isExploded){
            this.exploded = isExploded;
        };

        this.getExploded = function (){
            return exploded;
        };

        this.setX = function (position_x){
            this.x = position_x;
        };

        this.getX = function (){
            return x;
        };

        this.setY = function (position_y){
            this.y = position_y;
        };

        this.getY = function (){
            return y;
        };

        this.getDx  = function (){
            return dx;
        };

        this.getDy  = function (){
            return dy;
        };

        this.computeDeriverates = function(rotation){
            dx = -Math.sin(rotation)*TORPEDO_CONFIG.torpedo_ratio;
            dy = Math.cos(rotation)*TORPEDO_CONFIG.torpedo_ratio;
        }

    }