package de.mennomax.astikorcarts.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server (sender) -> Client (receiver) message
 */
public abstract class AbstractClientPlayMessage extends AbstractUnsidedPlayMessage implements IClientboundDistributor
{
    /**
     * This constructor should be called from message call site, ie. the code where you instantiate the message to send it to client
     *
     * @param type message type
     */
    public AbstractClientPlayMessage(final PlayMessageType<?> type)
    {
        super(type);
    }

    /**
     * In this constructor you deserialize received network payload. Formerly known as <code>#fromBytes(RegistryFriendlyByteBuf)</code>
     *
     * @param buf received network payload
     * @param type message type
     * @apiNote you can keep this protected to reduce visibility
     */
    protected AbstractClientPlayMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type)
    {
        super(type);
    }

    /**
     * Executes message action on main thread.
     *
     * @param context network context
     * @param player  client player which is receiving this packet
     */
    protected abstract void onExecute(final IPayloadContext context, final Player player);
}