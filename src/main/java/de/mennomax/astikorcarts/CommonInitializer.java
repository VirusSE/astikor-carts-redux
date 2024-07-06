package de.mennomax.astikorcarts;

import de.mennomax.astikorcarts.config.AstikorCartsConfig;
import de.mennomax.astikorcarts.entity.PostilionEntity;
import de.mennomax.astikorcarts.entity.ai.goal.PullCartGoal;
import de.mennomax.astikorcarts.entity.ai.goal.RideCartGoal;
import de.mennomax.astikorcarts.util.GoalAdder;
import de.mennomax.astikorcarts.world.AstikorWorld;
import de.mennomax.astikorcarts.world.SimpleAstikorWorld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class CommonInitializer implements Initializer {
    @Override
    public void init(final Context mod) {
        ModLoadingContext.get().register(ModConfig.Type.COMMON, AstikorCartsConfig.spec());
        mod.modBus().<FMLLoadCompleteEvent>addListener(e -> {
            AstikorCarts.LOGGER.info("Automatic pull animal configuration:\n{}", AstikorCartsConfig.Common.getComment());
        });
        mod.modBus().<EntityAttributeCreationEvent>addListener(e -> {
            e.put(AstikorCarts.EntityTypes.POSTILION.get(), LivingEntity.createLivingAttributes().build());
        });
        mod.bus().<AttachCapabilitiesEvent<Level>, Level>addGenericListener(Level.class, e ->
            e.addCapability(new ResourceLocation(AstikorCarts.ID, "astikor"), AstikorWorld.createProvider(SimpleAstikorWorld::new))
        );
        GoalAdder.mobGoal(Mob.class)
            .add(1, PullCartGoal::new)
            .add(1, RideCartGoal::new)
            .build()
            .register(mod.bus());
        mod.bus().<PlayerInteractEvent.EntityInteract>addListener(e -> {
            final Entity rider = e.getTarget().getControllingPassenger();
            if (rider instanceof PostilionEntity) {
                rider.stopRiding();
            }
        });
        mod.bus().<TickEvent.LevelTickEvent>addListener(e -> {
            if (e.phase == TickEvent.Phase.END) {
                AstikorWorld.get(e.level).ifPresent(AstikorWorld::tick);
            }
        });
    }
}
