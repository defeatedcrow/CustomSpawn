package defeatedcrow.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntitySpawnEventDC {

	// ONLY NATURAL SPAWN
	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getEntity() != null && event.getLevel() != null) {
			LevelAccessor level = event.getLevel();
			Mob mob = event.getEntity();
			if (!SpawnConfigJson.INSTANCE.configData.isEmpty()) {
				for (SpawnData data : SpawnConfigJson.INSTANCE.configData) {
					if (mob.getType().getCategory() != MobCategory.MISC && data.isMatchType(mob)) {
						// CustomSpawnCore.LOGGER.info("Find data " + data.getFileName());
						if (event.getSpawnReason() == MobSpawnType.SPAWNER && data.DenySpawner()) {
							event.setResult(Result.DENY);
						} else if (event.getSpawnReason() == MobSpawnType.NATURAL) {
							// 下のブロック
							BlockPos pos = new BlockPos(event.getX(), event.getY() - 0.5D, event.getZ());
							// 高度
							if (!data.isMatchAltitude(mob.blockPosition())) {
								event.setResult(Result.DENY);
								// CustomSpawnCore.LOGGER.info(data.getFileName() + ": DENY by altitude");
								break;
							} else if (data.isIncludeBiome(level.getBiome(pos))) {
								event.setResult(Result.DENY);
								// CustomSpawnCore.LOGGER.info(data.getFileName() + ": DENY by biome");
								break;
							} else if (data.isIncludeBlock(level.getBlockState(pos))) {
								event.setResult(Result.DENY);
								// CustomSpawnCore.LOGGER.info(data.getFileName() + ": DENY by block");
								break;
							} else if (data.DenyVillage()) {
								ServerLevel serverlevel = (ServerLevel) mob.level;
								if (serverlevel.isCloseToVillage(pos, 6)) {
									event.setResult(Result.DENY);
									// CustomSpawnCore.LOGGER.info(data.getFileName() + ": DENY by village");
									break;
								}
							}
						}
					}
				}
			}
		}
	}

}
