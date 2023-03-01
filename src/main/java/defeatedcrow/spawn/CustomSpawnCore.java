package defeatedcrow.spawn;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(CustomSpawnCore.MOD_ID)
public class CustomSpawnCore {
	public static final String MOD_ID = "dcs_custom_spawn";
	public static File configDir = null;
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public CustomSpawnCore() {
		configDir = new File(FMLPaths.CONFIGDIR.get().toFile() + "/custom_spawn");
		if (!configDir.exists()) {
			try {
				configDir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::commonSetup);
	}

	public void commonSetup(FMLCommonSetupEvent event) {
		SpawnConfigJson.INSTANCE.initFile();
		SpawnConfigJson.INSTANCE.loadFiles();
		MinecraftForge.EVENT_BUS.addListener(EntitySpawnEventDC::onSpawn);
	}

}
