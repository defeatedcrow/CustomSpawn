package defeatedcrow.spawn;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import defeatedcrow.spawn.config.DCSpawnConfig;
import defeatedcrow.spawn.config.SpawnBiomeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("dcs_spawn")
public final class CustomSpawnCore {

	public static final Logger LOGGER = LogManager.getLogger();

	public static CustomSpawnCore INSTANCE;
	public static final Path CONFIG_DIR = null;

	public CustomSpawnCore() {
		INSTANCE = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		MinecraftForge.EVENT_BUS.register(this);

		File config = FMLPaths.CONFIGDIR.get().toFile();
		SpawnBiomeList.INSTANCE.setDir(config);
		ModLoadingContext.get().registerConfig(Type.SERVER, DCSpawnConfig.INSTANCE.SPEC, "customspawn_core.toml");

	}

	public void setupCommon(final FMLCommonSetupEvent event) {
		DCSpawnConfig.INSTANCE.leadBlockNames();
		MinecraftForge.EVENT_BUS.register(new SpawnCustomEvent());
		MinecraftForge.EVENT_BUS.register(new LivingInVillageEvent());

		SpawnBiomeList.INSTANCE.pre();
		if (SpawnBiomeList.INSTANCE.biomeList.isEmpty()) {
			List<String> ret = ImmutableList.of("Biome name (ex. forest)", "or BiomeDictionaryType (ex. hot, dence)",
					"ALL specifies all biomes.");
			SpawnBiomeList.INSTANCE.addMobBlackList("RegistryName of Mobs. (ex. zombie, modID:modMobName)", ret);
		}
		SpawnBiomeList.INSTANCE.post();
	}
}
