package mod.azure.wotr.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class WoTRConfig extends MidnightConfig {

	@Entry
	public static double drake_health = 80;
	@Entry
	public static int drake_armor = 4;
	@Entry
	public static double drake_melee = 13;
	@Entry
	public static float drake_ranged = 15;
	@Entry
	public static int drake_exp = 30;

	@Entry
	public static double lung_serpent_health = 80;
	@Entry
	public static int lung_serpent_exp = 30;
}
