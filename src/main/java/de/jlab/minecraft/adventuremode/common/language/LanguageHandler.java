package de.jlab.minecraft.mods.adventuremode.language;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LanguageHandler {

	private static final String LANG_RESOURCE_LOCATION = "/mods/Top/lang/";
	public static String[] localeFiles = {LANG_RESOURCE_LOCATION + "en_US.xml"};
	
	public void loadLocalizations() {
		//LanguageRegistry.instance().loadLocalization(localizationFile, lang, isXML);
	}
	
}
