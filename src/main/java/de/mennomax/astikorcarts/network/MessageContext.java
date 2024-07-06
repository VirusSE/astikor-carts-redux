package de.mennomax.astikorcarts.network;

import net.neoforged.fml.LogicalSide;

public abstract class MessageContext {
    protected final NetworkEvent.Context context;

    public MessageContext(final NetworkEvent.Context context) {
        this.context = context;
    }

    public abstract LogicalSide getSide();
}
