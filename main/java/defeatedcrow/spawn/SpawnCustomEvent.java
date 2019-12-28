package defeatedcrow.spawn;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.village.Village;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnCustomEvent {

	@SubscribeEvent
	public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		EntityLivingBase living = event.getEntityLiving();
		float y = event.getY();
		if (living != null && !living.world.isRemote && living instanceof EntityLiving) {
			// normal spawn
			boolean b1 = !event.isSpawner();
			if (!DCSpawnConfig.allowSpawner) {
				b1 = true;
			}
			if (b1 && y > DCSpawnConfig.deleteMin && y <= DCSpawnConfig.deleteMax) {
				event.setResult(Result.DENY);
			} else {
				boolean b = false;
				if (b1 && y > DCSpawnConfig.reduceMin && y <= DCSpawnConfig.reduceMax) {
					if (event.getWorld().rand.nextFloat() < 0.5F) {
						b = true;
					}
				}

				if (b1 && !b) {
					IBlockState state = event.getWorld().getBlockState(living.getPosition().down());
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

				if (DCSpawnConfig.deleteInVillage && !b && event.getWorld().getVillageCollection() != null) {
					for (Village vil : event.getWorld().getVillageCollection().getVillageList()) {
						if (vil.getCenter().distanceSq(living.getPosition()) < 96D * 96D) {
							b = true;
						}
					}
				}

				if (b) {
					event.setResult(Result.DENY);
				}
			}
		}
	}

	static PotionEffect effectLoot(EntityLivingBase living) {
		Potion potion = MobEffects.INVISIBILITY;
		int r = living.world.rand.nextInt(5);
		if (r == 0) {
			potion = MobEffects.SPEED;
		} else if (r == 1) {
			potion = MobEffects.STRENGTH;
		} else if (r == 2) {
			potion = MobEffects.HEALTH_BOOST;
		}
		return new PotionEffect(potion, 2400, 1);
	}

}
