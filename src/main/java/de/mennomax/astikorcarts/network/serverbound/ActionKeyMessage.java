package de.mennomax.astikorcarts.network.serverbound;

import com.google.common.base.MoreObjects;
import com.mojang.datafixers.util.Pair;
import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import de.mennomax.astikorcarts.entity.SupplyCartEntity;
import de.mennomax.astikorcarts.network.AbstractServerPlayMessage;
import de.mennomax.astikorcarts.network.MessageOld;
import de.mennomax.astikorcarts.network.PlayMessageType;
import de.mennomax.astikorcarts.network.ServerMessageContextOldOld;
import de.mennomax.astikorcarts.world.AstikorWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Comparator;
import java.util.Optional;

public class ActionKeyMessage extends AbstractServerPlayMessage {

    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(AstikorCarts.ID, "action_key_to_server", ActionKeyMessage::new);

    public ActionKeyMessage() { // insert some specific parameters here, which needs to be explicitly stored in the message
        super(TYPE);
        // this.parameter = paramter
    }

    public ActionKeyMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type)  {
        super(buf, type);
        // this.banner = deserializeCodeDingens(buf);
    }

    @Override
    protected void toBytes(RegistryFriendlyByteBuf buf) {
        // nüscht zu tun!
    }

    @Override
    protected void onExecute(IPayloadContext ctxIn, final ServerPlayer player) {
        final Entity pulling = MoreObjects.firstNonNull(player.getVehicle(), player);
        final Level world = player.level();
        AstikorWorld.get(world).map(w -> w.getDrawn(pulling)).orElse(Optional.empty())
            .map(c -> Pair.of(c, (Entity) null))
            .or(() -> world.getEntitiesOfClass(AbstractDrawnEntity.class, pulling.getBoundingBox().inflate(2.0D), entity -> entity != pulling).stream()
                .min(Comparator.comparing(pulling::distanceTo))
                .map(c -> Pair.of(c, pulling))
            ).ifPresent(p -> p.getFirst().setPulling(p.getSecond()));
    }
}
/*
public final class ActionKeyMessageOld implements MessageOld {
    @Override
    public void encode(final FriendlyByteBuf buf) {
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
    }

    public static void handle(final ActionKeyMessageOld msg, final ServerMessageContextOldOld ctx) {
        final ServerPlayer player = ctx.getPlayer();
        final Entity pulling = MoreObjects.firstNonNull(player.getVehicle(), player);
        final Level world = player.level();
        AstikorWorld.get(world).map(w -> w.getDrawn(pulling)).orElse(Optional.empty())
                .map(c -> Pair.of(c, (Entity) null))
                .or(() -> world.getEntitiesOfClass(AbstractDrawnEntity.class, pulling.getBoundingBox().inflate(2.0D), entity -> entity != pulling).stream()
                        .min(Comparator.comparing(pulling::distanceTo))
                        .map(c -> Pair.of(c, pulling))
                ).ifPresent(p -> p.getFirst().setPulling(p.getSecond()));
    }
}*/
