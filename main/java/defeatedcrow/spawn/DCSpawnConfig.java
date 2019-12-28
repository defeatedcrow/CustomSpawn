package defeatedcrow.spawn;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class DCSpawnConfig {

	private DCSpawnConfig() {}

	public static final DCSpawnConfig INSTANCE = new DCSpawnConfig();

	// key
	public static int deleteMin = 50;
	public static int deleteMax = 80;

	public static int reduceMin = 40;
	public static int reduceMax = 100;

	public static boolean allowSpawner = true;
	public static boolean deleteInVillage = true;

	public static String[] updateBlackList = new String[] {
			"minecraft:log:32767", "minecraft:log2:32767", "minecraft:planks:32767", "ModID:sampleBlock:sampleMeta"
	};
	public static final List<BlockSet> blacklistBlock = Lists.newArrayList();

	// public static boolean harderSpawner = true;

	public void load(Configuration cfg) {

		try {
			cfg.load();

			Property delM = cfg.get("setting", "SpawnDeleteHeight_Min", deleteMin);
			Property delX = cfg.get("setting", "SpawnDeleteHeight_Max", deleteMax);

			Property redM = cfg.get("setting", "SpawnReduceHeight_Min", reduceMin);
			Property redX = cfg.get("setting", "SpawnReduceHeight_Max", reduceMax);

			Property spawner = cfg.get("setting", "AllowSpawnerSpawn", allowSpawner,
					"Enable spawn mobs from the spawner block.");

			// Property harder = cfg.get("setting", "EnableHarderSpawner", harderSpawner,
			// "Add some potion effects for mobs from the spawner block.");

			Property village = cfg.get("setting", "DeleteZonbieInVillage", deleteInVillage,
					"Delete zonbies that entered the village.");

			Property b_list = cfg.get("setting", "SpawnBlacklistBlocks", updateBlackList,
					"Blacklist to prevent enemy mobs from spawning above.");

			deleteMin = delM.getInt();
			deleteMax = delX.getInt();
			reduceMin = redM.getInt();
			reduceMax = redX.getInt();
			allowSpawner = spawner.getBoolean();
			// harderSpawner = harder.getBoolean();
			deleteInVillage = village.getBoolean();
			updateBlackList = b_list.getStringList();

			leadBlockNames();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cfg.save();
		}

	}

	public static void leadBlockNames() {
		blacklistBlock.clear();
		blacklistBlock.addAll(getListFromStrings(updateBlackList, "SpawnBlacklist"));
	}

	public boolean isInBlacklist(IBlockState state) {
		if (state == null)
			return false;
		BlockSet set = new BlockSet(state.getBlock(), state.getBlock().getMetaFromState(state));
		return blacklistBlock.contains(set);
	}

	public class BlockSet {

		public final Block block;
		public final int meta;

		public BlockSet(Block i, int j) {
			block = i;
			meta = j;
		}

		public IBlockState getState() {
			return block == null ? Blocks.AIR.getDefaultState() : block.getStateFromMeta(meta);
		}

		/**
		 * metaにはwildcard指定可能
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof BlockSet) {
				BlockSet p = (BlockSet) obj;
				return p.block == block && (meta == 32767 || p.meta == 32767 || p.meta == meta);
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			int i = block.getUnlocalizedName().hashCode() + meta;
			return i;
		}

		@Override
		public String toString() {
			String name = block == null ? "null" : block.toString();
			return name + ":" + meta;
		}
	}

	private static final Logger LOGGER = LogManager.getLogger("dcs_spawn");

	public static List<BlockSet> getListFromStrings(String[] names, String logname) {
		List<BlockSet> list = Lists.newArrayList();
		if (names != null && names.length > 0) {
			for (String name : names) {
				if (name != null) {
					String itemName = name;
					String modid = "minecraft";
					int meta = 32767;
					if (name.contains(":")) {
						String[] n2 = name.split(":");
						if (n2 != null && n2.length > 0) {
							if (n2.length > 2) {
								Integer m = null;
								try {
									m = Integer.parseInt(n2[2]);
								} catch (NumberFormatException e) {
									LOGGER.debug("Tried to parse non Integer target: " + n2[2]);
								}
								if (m != null && m >= 0) {
									meta = m;
								}
							}

							if (n2.length == 1) {
								itemName = n2[0];
							} else {
								modid = n2[0];
								itemName = n2[1];
							}
						}
					}

					Block block = Block.REGISTRY.getObject(new ResourceLocation(modid, itemName));
					if (block != null && block != Blocks.AIR) {
						LOGGER.debug(logname + " add target: " + modid + ":" + itemName + ", " + meta);
						BlockSet set = INSTANCE.new BlockSet(block, meta);
						list.add(set);
					}
				}
			}
		}
		return list;
	}

}
