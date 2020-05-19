package defeatedcrow.spawn;

import java.util.Optional;

import defeatedcrow.spawn.config.DCSpawnConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingInVillageEvent {

	@SubscribeEvent
	public void onLiving(LivingUpdateEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (DCSpawnConfig.INSTANCE.deleteInVillage.get() && living != null && living instanceof ZombieEntity) {
			if (!living.getEntityWorld().isRemote) {
				ServerWorld world = (ServerWorld) living.getEntityWorld();
				Optional<BlockPos> pos = world.getPointOfInterestManager()
						.findClosest(PointOfInterestType.MATCH_ANY, p -> {
							return true;
						}, living.getPosition(), 32, PointOfInterestManager.Status.ANY);
				if (pos.isPresent() && pos.get().distanceSq(living.getPosition()) < 32D * 32D) {
					living.remove();
				}
			}
		}
	}

}
