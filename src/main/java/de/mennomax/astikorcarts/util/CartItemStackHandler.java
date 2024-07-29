package de.mennomax.astikorcarts.util;

import de.mennomax.astikorcarts.entity.AbstractDrawnEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CartItemStackHandler<T extends AbstractDrawnEntity> extends ItemStackHandler {
    protected final T cart;

    public CartItemStackHandler(final int slots, final T cart) {
        super(slots);
        this.cart = cart;
    }


    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        nbt.remove("Size");
        super.deserializeNBT(provider, nbt);
    }
}
