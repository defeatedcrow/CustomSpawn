package defeatedcrow.spawn;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CustomSpawnCore.MOD_ID, name = CustomSpawnCore.MOD_NAME, version = CustomSpawnCore.MOD_MEJOR + "."
		+ CustomSpawnCore.MOD_MINOR + "."
		+ CustomSpawnCore.MOD_BUILD, dependencies = CustomSpawnCore.MOD_DEPENDENCIES, acceptedMinecraftVersions = CustomSpawnCore.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)
public class CustomSpawnCore {
	public static final String MOD_ID = "dcs_spawn";
	public static final String MOD_NAME = "DCsCustomSpawnMod";
	public static final int MOD_MEJOR = 2;
	public static final int MOD_MINOR = 1;
	public static final int MOD_BUILD = 0;
	public static final String MOD_DEPENDENCIES = "";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.11,1.12.2]";

	@Instance("dcs_spawn")
	public static CustomSpawnCore instance;

	public static final String PACKAGE_BASE = "dcs";
	public static final String PACKAGE_ID = "dcs_spawn";

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side) {
		return true;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File cfgFile = new File(event.getModConfigurationDirectory(), "defeatedcrow/customspawn/core.cfg");
		DCSpawnConfig.INSTANCE.load(new Configuration(cfgFile));
		SpawnBiomeList.INSTANCE.setDir(event.getModConfigurationDirectory());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new SpawnCustomEvent());
		MinecraftForge.EVENT_BUS.register(new LivingInVillageEvent());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		SpawnBiomeList.INSTANCE.pre();
		if (SpawnBiomeList.INSTANCE.biomeList.isEmpty()) {
			List<String> ret = ImmutableList.of("Biome name (ex. forest)", "or BiomeDictionaryType (ex. hot, dence)",
					"ALL specifies all biomes.");
			SpawnBiomeList.INSTANCE.addMobBlackList("RegistryName of Mobs. (ex. zombie, modID:modMobName)", ret);
		}
		SpawnBiomeList.INSTANCE.post();
	}

	@EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {}
}
