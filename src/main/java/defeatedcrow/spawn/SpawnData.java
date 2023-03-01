package defeatedcrow.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class SpawnData {
	private final String EntityTypeName;
	private final int MaxSpawnAltitude;
	private final int MinSpawnAltitude;
	private final boolean DenySpawnInVillage;
	private final boolean DenySpawnerSpawn;
	private final String[] BiomeTagBlacklist;
	private final String[] BlockTagBlacklist;

	public SpawnData(EntityType<?> type, int x, int m, boolean v, boolean d, String[] biomes, String[] blocks) {
		ResourceLocation res = ForgeRegistries.ENTITY_TYPES.getKey(type);
		EntityTypeName = res == null ? "empty" : res.getPath().toString().toLowerCase();
		MaxSpawnAltitude = x;
		MinSpawnAltitude = m;
		DenySpawnInVillage = v;
		DenySpawnerSpawn = d;
		BiomeTagBlacklist = biomes;
		BlockTagBlacklist = blocks;
	}

	public String getFileName() {
		return EntityTypeName;
	}

	public boolean DenyVillage() {
		return DenySpawnInVillage;
	}

	public boolean DenySpawner() {
		return DenySpawnerSpawn;
	}

	public boolean isMatchType(Entity entity) {
		if (entity == null || entity.getType() == null)
			return false;
		ResourceLocation name = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
		if (name != null && name.toString().toLowerCase().contains(EntityTypeName))
			return true;
		return false;
	}

	public boolean isMatchAltitude(BlockPos pos) {
		if (MaxSpawnAltitude < 320 && pos.getY() > MaxSpawnAltitude)
			return false;
		if (MinSpawnAltitude > -64 && pos.getY() < MinSpawnAltitude)
			return false;
		return true;
	}

	public boolean isIncludeBiome(Holder<Biome> biome) {
		if (BiomeTagBlacklist != null && BiomeTagBlacklist.length > 0) {
			for (String name : BiomeTagBlacklist) {
				if (biome.getTagKeys().anyMatch(tag -> isMatch(name, tag)))
					return true;
			}
		}
		return false;
	}

	public boolean isIncludeBlock(BlockState state) {
		if (BlockTagBlacklist != null && BlockTagBlacklist.length > 0) {
			for (String name : BlockTagBlacklist) {
				if (state.getTags().anyMatch(tag -> isMatch(name, tag)))
					return true;
			}
		}
		return false;
	}

	public static boolean isMatch(String name, TagKey<?> tag) {
		return tag.location().toString().toLowerCase().contains(name);
	}

}
