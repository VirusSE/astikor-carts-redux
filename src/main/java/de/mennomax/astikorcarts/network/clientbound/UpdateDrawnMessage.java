package de.mennomax.astikorcarts.network.clientbound;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import de.mennomax.astikorcarts.network.AbstractClientPlayMessage;
import de.mennomax.astikorcarts.network.ClientMessageContextOld;
import de.mennomax.astikorcarts.network.MessageOld;
import de.mennomax.astikorcarts.network.PlayMessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiConsumer;

public class UpdateDrawnMessage extends AbstractClientPlayMessage {

    public static final PlayMessageType<?> TYPE = PlayMessageType.forClient(AstikorCarts.ID, "update_draw_bidirectional", UpdateDrawnMessage::new);

    private int pullingId;
    private int cartId;

    public UpdateDrawnMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type) {
        super(buf, type);
        this.pullingId = buf.readInt();
        this.cartId = buf.readInt();
    }

    public UpdateDrawnMessage(final int pullingId, final int cartId) {
        super(TYPE);
        this.pullingId = pullingId;
        this.cartId = cartId;
    }

    @Override
    public void toBytes(final RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.pullingId);
        buf.writeVarInt(this.cartId);
    }

    @Override
    public void onExecute(IPayloadContext ctx, final Player player) {
        final Level world = player.level();
        final Entity e = world.getEntity(this.cartId);
        if (e instanceof AbstractDrawnEntity) {
            if (this.pullingId < 0) {
                ((AbstractDrawnEntity) e).setPulling(null);
            } else {
                ((AbstractDrawnEntity) e).setPulling(world.getEntity(this.pullingId));
            }
        }
    }
}
/*
@Deprecated
public final class UpdateDrawnMessageOld implements MessageOld {
    private int pullingId;

    private int cartId;

    public UpdateDrawnMessageOld() {
    }

    public UpdateDrawnMessageOld(final int pullingId, final int cartId) {
        this.pullingId = pullingId;
        this.cartId = cartId;
    }

    @Override
    public void encode(final FriendlyByteBuf buf) {
        buf.writeVarInt(this.pullingId);
        buf.writeVarInt(this.cartId);
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
        this.pullingId = buf.readVarInt();
        this.cartId = buf.readVarInt();
    }

    public static final class Handler implements BiConsumer<UpdateDrawnMessageOld, ClientMessageContextOld> {
        @Override
        public void accept(final UpdateDrawnMessageOld msg, final ClientMessageContextOld ctx) {
            final Level world = ctx.getWorld();
            final Entity e = world.getEntity(msg.cartId);
            if (e instanceof AbstractDrawnEntity) {
                if (msg.pullingId < 0) {
                    ((AbstractDrawnEntity) e).setPulling(null);
                } else {
                    ((AbstractDrawnEntity) e).setPulling(world.getEntity(msg.pullingId));
                }
            }
        }
    }
}*/
