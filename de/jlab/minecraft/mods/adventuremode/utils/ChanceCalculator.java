package de.jlab.minecraft.mods.adventuremode.utils;

import java.util.Random;

public class ChanceCalculator {
	    
    private Random random;
    
    public ChanceCalculator() {
        reset();
    }
    
    public boolean calculateChance(float probability) {
        return this.random.nextInt(100) + 1 < probability;
    }
    
    public boolean calculateChance(double probability) {
        return this.random.nextInt(100) + 1 < probability;
    }
    
    public void reset() {
        this.random = new Random();
    }
	    
}
