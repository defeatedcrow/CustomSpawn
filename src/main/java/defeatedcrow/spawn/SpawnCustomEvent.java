package defeatedcrow.spawn;

import java.util.Optional;

import defeatedcrow.spawn.config.DCSpawnConfig;
import defeatedcrow.spawn.config.SpawnBiomeList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnCustomEvent {

	@SubscribeEvent
	public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		LivingEntity living = event.getEntityLiving();
		double y = event.getY();
		if (living != null && !living.world.isRemote) {
			// normal spawn
			boolean b1 = !event.isSpawner() || !DCSpawnConfig.INSTANCE.allowSpawner.get();
			boolean b2 = living instanceof IMob;
			if (b1 && b2 && y > DCSpawnConfig.INSTANCE.deleteMin.get() && y <= DCSpawnConfig.INSTANCE.deleteMax.get()) {
				event.setResult(Result.DENY);
			} else {
				boolean b = false;
				if (b1 && b2 && y > DCSpawnConfig.INSTANCE.reduceMin.get() && y <= DCSpawnConfig.INSTANCE.reduceMax
						.get()) {
					if (event.getWorld().getRandom().nextFloat() < 0.5F) {
						b = true;
					}
				}

				if (b1 && b2 && !b) {
					BlockState state = event.getWorld().getBlockState(living.getPosition().down());
					if (DCSpawnConfig.INSTANCE.isInBlacklist(state)) {
						b = true;
					}
				}

				if (b1 && !b) {
					Biome biome = living.getEntityWorld().getBiome(living.getPosition());
					if (!SpawnBiomeList.INSTANCE.canSpawn(living, biome)) {
						b = true;
					}
				}

				if (DCSpawnConfig.INSTANCE.deleteInVillage.get() && !b && b2 && !event.getWorld().isRemote()) {
					ServerWorld world = (ServerWorld) living.getEntityWorld();
					Optional<BlockPos> pos = world.getPointOfInterestManager().findClosest(
							PointOfInterestType.MATCH_ANY, p -> {
								return true;
							}, living.getPosition(), 64, PointOfInterestManager.Status.ANY);
					if (pos.isPresent() && pos.get().distanceSq(living.getPosition()) < 64D * 64D) {
						b = true;
					}
				}
				if (b) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	static EffectInstance effectLoot(LivingEntity living) {
		Effect potion = Effects.INVISIBILITY;
		int r = living.world.rand.nextInt(5);
		if (r == 0) {
			potion = Effects.SPEED;
		} else if (r == 1) {
			potion = Effects.STRENGTH;
		} else if (r == 2) {
			potion = Effects.HEALTH_BOOST;
		} else if (r == 3) {
			potion = Effects.RESISTANCE;
		}
		return new EffectInstance(potion, 2400, 0);
	}

}
