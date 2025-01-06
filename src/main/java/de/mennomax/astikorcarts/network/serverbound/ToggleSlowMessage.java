package de.mennomax.astikorcarts.network.serverbound;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import de.mennomax.astikorcarts.network.AbstractServerPlayMessage;
import de.mennomax.astikorcarts.network.MessageOld;
import de.mennomax.astikorcarts.network.PlayMessageType;
import de.mennomax.astikorcarts.network.ServerMessageContextOldOld;
import de.mennomax.astikorcarts.world.AstikorWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ToggleSlowMessage extends AbstractServerPlayMessage {

    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(AstikorCarts.ID, "toggle_slow_to_server", ToggleSlowMessage::new);

    public ToggleSlowMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type) {
        super(buf, type);
    }

    public ToggleSlowMessage() {
        super(TYPE);
    }

    @Override
    protected void toBytes(@NotNull final RegistryFriendlyByteBuf buf) {
        // nothing to do here!
    }

    @Override
    public void onExecute(final IPayloadContext ctxIn, final ServerPlayer player) {
        getCart(player).ifPresent(AbstractDrawnEntity::toggleSlow);
    }

    public static Optional<AbstractDrawnEntity> getCart(final Player player) {
        final Entity ridden = player.getVehicle();
        if (ridden == null) return Optional.empty();
        if (ridden instanceof AbstractDrawnEntity) return Optional.of((AbstractDrawnEntity) ridden);
        return AstikorWorld.get(ridden.level()).resolve().flatMap(w -> w.getDrawn(ridden));
    }
}
/*
@Deprecated
public final class ToggleSlowMessageOld implements MessageOld {
    @Override
    public void encode(final FriendlyByteBuf buf) {
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
    }

    public static void handle(final ToggleSlowMessage msg, final ServerMessageContextOldOld ctx) {
        getCart(ctx.getPlayer()).ifPresent(AbstractDrawnEntity::toggleSlow);
    }

    public static Optional<AbstractDrawnEntity> getCart(final Player player) {
        final Entity ridden = player.getVehicle();
        if (ridden == null) return Optional.empty();
        if (ridden instanceof AbstractDrawnEntity) return Optional.of((AbstractDrawnEntity) ridden);
        return AstikorWorld.get(ridden.level()).resolve().flatMap(w -> w.getDrawn(ridden));
    }
}*/
