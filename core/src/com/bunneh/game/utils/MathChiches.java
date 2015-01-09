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

	public static float approach(float goal, float current, float delta) {
		float diff = goal - current;
		if(diff > delta) {
			return current + delta;
		}
		if(diff < -delta) {
			return current - delta;
		}
		
		return goal;
	}
	
	public static Vector2 vectorToTarget(float x, float y, float targetX, float targetY) {
		Vector2 v = new Vector2();
		v.x = (targetX-x);
		v.y = (targetY-y);
		return v;
	}
	
	public static float distance(float x1, float y1, float x2, float y2) {
		double distance = Math.sqrt((x2 - x1) *(x2-x1) + (y2 - y1) * (y2-y1));
		return (float) distance;
	}
	

}
