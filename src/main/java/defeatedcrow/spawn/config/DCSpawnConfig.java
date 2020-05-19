package defeatedcrow.spawn.config;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import defeatedcrow.spawn.CustomSpawnCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class DCSpawnConfig {

	public static final DCSpawnConfig INSTANCE = new DCSpawnConfig();

	public final ForgeConfigSpec SPEC;

	public final ForgeConfigSpec.IntValue deleteMin;
	public final ForgeConfigSpec.IntValue deleteMax;

	public final ForgeConfigSpec.IntValue reduceMin;
	public final ForgeConfigSpec.IntValue reduceMax;

	public final ForgeConfigSpec.BooleanValue allowSpawner;
	public final ForgeConfigSpec.BooleanValue deleteInVillage;

	public final ForgeConfigSpec.ConfigValue<List<String>> blackList;

	public DCSpawnConfig() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("core");

		deleteMin = builder.defineInRange("SpawnDeleteHeightMin", 50, 1, 254);
		deleteMax = builder.defineInRange("SpawnDeleteHeightMax", 80, 1, 254);
		reduceMin = builder.defineInRange("SpawnReduceHeightMin", 40, 1, 254);
		reduceMax = builder.defineInRange("SpawnReduceHeightMax", 100, 1, 254);

		allowSpawner = builder.comment("Enable spawn mobs from the spawner block.").define("AllowSpawnerSpawn", true);
		deleteInVillage = builder.comment("Delete zonbies that entered the village.").define("AllowSpawnerSpawn", true);

		blackList = builder.comment("Blacklist to prevent enemy mobs from spawning above.").define(
				"SpawnBlacklistBlocks", updateBlackList);

		builder.pop();
		SPEC = builder.build();
	}

	// key

	public final List<String> updateBlackList = ImmutableList.of("oak_wood", "oak_planks", "spruce_planks",
			"birch_planks", "jungle_planks", "acacia_planks", "dark_oak_planks", "modid:sampleblock");

	public final List<Block> blacklistBlock = Lists.newArrayList();

	public void leadBlockNames() {
		INSTANCE.blacklistBlock.clear();
		INSTANCE.blacklistBlock.addAll(getListFromStrings(INSTANCE.blackList.get()));
	}

	public boolean isInBlacklist(BlockState state) {
		if (state == null)
			return false;
		return INSTANCE.blacklistBlock.contains(state.getBlock());
	}

	public static List<Block> getListFromStrings(List<String> updateblacklist) {
		List<Block> list = Lists.newArrayList();
		if (updateblacklist != null && updateblacklist.size() > 0) {
			for (String name : updateblacklist) {
				if (name != null) {
					String itemName = name;
					String modid = "minecraft";
					if (name.contains(":")) {
						String[] n2 = name.split(":");
						if (n2 != null) {
							if (n2.length == 1) {
								itemName = n2[0];
							} else if (n2.length > 1) {
								modid = n2[0];
								itemName = n2[1];
							}
						}
					}

					if (modid != null && ModList.get().isLoaded(modid)) {
						CustomSpawnCore.LOGGER.debug("SpawnBlacklist add target: " + modid + ":" + itemName);
						Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid, itemName));
						if (block != null && block != Blocks.AIR) {
							list.add(block);
						}
					}
				}
			}
		}
		return list;
	}

}
