package de.jlab.minecraft.adventuremode.utils;

import java.util.Random;

public class RandomRange {

	private ChanceCalculator chance = new ChanceCalculator(); 
	private Random rand = new Random();
	
	public int range(int min, int max) {	    
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public int weightedRange(int min, int max, int ... chances) {
		float range = max - min;
		int part = Math.round(range / chances.length);
		
		for (int i = 0; i < chances.length; i++) {
			if (chance.calculateChance(chances[i])) {
				return range(min + part * i, max + (part * i + 1));
			}
		}
		
		return range(min, max);
	}
	
}
