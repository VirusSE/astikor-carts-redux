package de.mennomax.astikorcarts.server.event;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.config.AstikorCartsConfig;
import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod(AstikorCarts.ID)
public class ServerEvents {

    @SubscribeEvent
    public static void entityStruckByLightningEvent(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof AbstractDrawnEntity && AstikorCartsConfig.get().lightningInvulnerable.get()) {
            event.setCanceled(true);
        }
    }
}