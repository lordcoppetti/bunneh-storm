package com.bunneh.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class MathChiches {
	
	private MathChiches() {}
	
	public static Vector2 getAngleBasedMovement(float angle, float speed) {
        Vector2 v = new Vector2();
        v.x = (float) MathUtils.cos( (angle*MathUtils.PI/180f))*speed;
        v.y = (float) MathUtils.sin( (angle*MathUtils.PI/180))*speed;
        return v;
    }

    public static double getAngle(float x1, float y1, float targetX, float targetY) {
        float x = targetX - x1;
        float y = targetY - y1;
        double angle;
        angle = MathUtils.atan2(y, x) * (180 / MathUtils.PI);
        return angle;
    }

}
