package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Adapter, którego istnienie uzasadnia niespójność nazewnictwa w jsonach i klasie Torpedo
 * Umożliwia zamianę torpedy w jsonie od klienta do klasy Torpedy, którą operuje się po stronie serwera
 */

public class TorpedoAdapter {
    private Torpedo torpedo = new Torpedo();
    public Torpedo getTorpedo() {
        return torpedo;
    }
    @JsonProperty("current_x")
    public void setCurrent_x(double current_x) { torpedo.setX(current_x); }
    public double current_x;
    @JsonProperty("current_y")
    public void setCurrent_y(double current_y) {
        torpedo.setY(current_y);
    }
    @JsonProperty("x_prim")
    public void setX_prim(double x_prim) {
        torpedo.setDeltaX(x_prim);
    }
    @JsonProperty("y_prim")
    public void setY_prim(double y_prim) {
        torpedo.setDeltaY(y_prim);
    }
}
