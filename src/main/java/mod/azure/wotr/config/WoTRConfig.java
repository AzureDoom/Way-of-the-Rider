package mod.azure.wotr.config;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import mod.azure.wotr.WoTRMod;

@Config(id = WoTRMod.MODID)
public class WoTRConfig {

	@Configurable
	public  double drake_health = 80;
	@Configurable
	public  int drake_armor = 4;
	@Configurable
	public  double drake_melee = 13;
	@Configurable
	public  float drake_ranged = 15;
	@Configurable
	public  int drake_exp = 30;

	@Configurable
	public  double lung_serpent_health = 80;
	@Configurable
	public  int lung_serpent_exp = 30;
}
