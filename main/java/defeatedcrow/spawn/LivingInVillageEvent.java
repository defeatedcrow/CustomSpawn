package defeatedcrow.spawn;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.village.Village;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingInVillageEvent {

	@SubscribeEvent
	public void onLiving(LivingUpdateEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (DCSpawnConfig.deleteInVillage && living != null && living instanceof EntityZombie) {
			if (living.world.getVillageCollection() != null) {
				for (Village vil : living.world.getVillageCollection().getVillageList()) {
					if (vil.getCenter().distanceSq(living.getPosition()) < 32D * 32D) {
						living.setDead();
					}
				}
			}
		}
	}

}
