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
	
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

}

