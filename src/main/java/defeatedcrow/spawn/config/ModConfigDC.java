package defeatedcrow.spawn.config;

import java.nio.file.Path;
import java.util.function.Function;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class ModConfigDC extends ModConfig {

	public ModConfigDC(Type type, ForgeConfigSpec spec, String fileName) {
		super(type, spec, ModLoadingContext.get().getActiveContainer(), fileName);
	}

	@Override
	public ConfigFileTypeHandler getHandler() {
		return new ConfigFileTypeHandlerDC();
	}

	private static class ConfigFileTypeHandlerDC extends ConfigFileTypeHandler {

		@Override
		public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
			return super.reader(getPath(configBasePath));
		}

		@Override
		public void unload(Path configBasePath, ModConfig config) {
			super.unload(getPath(configBasePath), config);
		}

		private static Path getPath(Path configBasePath) {
			if (configBasePath.endsWith("serverconfig")) {
				return FMLPaths.CONFIGDIR.get();
			}
			return configBasePath;
		}

	}

}
