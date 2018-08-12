package de.jlab.minecraft.mods.adventuremode.entity.monster;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public interface EntityBoss {

	// RANGEDATTRIBUTE(<name>, <default>, <min>, <max>) ;
	
	public static final Attribute scale = (new RangedAttribute("generic.scale", 3.0D, 0.0D, Double.MAX_VALUE)).func_111117_a("Scale").func_111112_a(true);
	
}
