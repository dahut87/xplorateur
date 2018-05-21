package fr.meconnu.calc;

public class Maths {
	static public float LegalizeDegree(float value) {
        while (value > 360) {
            value = value - 360;
        }

        while (value < 0) {
            value += 360;
        }

        return value;
}
	
	public static float linearInterpolation(float min, float max, float min2, float max2, float value) {
	        float range1 = max - min;
	        float range2 = max2 - min2;
	        return ((value - min) * range2 / range1) + min2;
	    }

}

