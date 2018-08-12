package de.jlab.minecraft.adventuremode.utils;

public final class Easing {

	public static float circular(float damage, float maxDamage) {
		damage /= maxDamage;
		return (float)(Math.sqrt(1 - damage * damage));
	}
	
}
