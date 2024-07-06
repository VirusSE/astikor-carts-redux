package de.mennomax.astikorcarts.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;

import java.util.Objects;

public class ServerMessageContext extends MessageContext {
    public ServerMessageContext(final NetworkEvent.Context context) {
        super(context);
    }

    @Override
    public LogicalSide getSide() {
        return null;
    }


    public MinecraftServer getServer() {
        return this.getPlayer().server;
    }

    public ServerLevel getWorld() {
        return this.getPlayer().server.overworld();
    }

    public ServerPlayer getPlayer() {
        return Objects.requireNonNull(this.context.getSender());
    }
}
