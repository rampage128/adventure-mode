package de.jlab.minecraft.adventuremode.common.event;

import de.jlab.minecraft.adventuremode.common.event.boss.BossEventGenerator;
import de.jlab.minecraft.adventuremode.common.event.invasion.InvasionEventGenerator;

public enum EventType {
	INVASION (InvasionEventGenerator.class),
	BOSS (BossEventGenerator.class);
	
	private Class<? extends EventGenerator> generatorType;
	
	EventType(Class<? extends EventGenerator> generatorType) {
		this.generatorType = generatorType;
	}
	
	public boolean isOfType(EventGenerator generator) {
		return this.generatorType.isInstance(generator);
	}
	
	public EventGenerator getGenerator() throws InstantiationException, IllegalAccessException {
		return this.generatorType.newInstance();
	}
}