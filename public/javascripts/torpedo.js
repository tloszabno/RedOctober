    function Torpedo(position_x, position_y, exploded){
        var x = position_x;
        var y = position_y;
        var exploded = exploded;

        this.setExploded = function(isExploded){
            this.exploded = isExploded;
        }

        this.getExploded = function (){
            return exploded;
        }

        this.setX = function (position_x){
            this.x = position_x;
        }

        this.getX = function (){
            return x;
        }

        this.setY = function (position_y){
            this.y = position_y;
        }

        this.getY = function (){
            return y;
        }
    }