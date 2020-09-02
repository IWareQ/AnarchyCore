package Anarchy.Utils;

import java.util.SplittableRandom;

public class RandomUtils {
	public static final SplittableRandom random = new SplittableRandom();
	
	public static int rand(int min, int max) {
		if (min == max) {
			return max;
		}
		return random.nextInt(max + 1 - min) + min;
	}
	
	public static double rand(double min, double max) {
		if (min == max) {
			return max;
		}
		return min + Math.random() * (max - min);
	}
	
	public static float rand(float min, float max) {
		if (min == max) {
			return max;
		}
		return min + (float)Math.random() * (max - min);
	}
	
	public static boolean rand() {
		return random.nextBoolean();
	}
}