package de.mennomax.astikorcarts.entity;

import de.mennomax.astikorcarts.util.CartItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractDrawnInventoryEntity extends AbstractDrawnEntity {
    private static final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public ItemStackHandler inventory = this.initInventory();
    private LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> this.inventory);

    public AbstractDrawnInventoryEntity(final EntityType<? extends Entity> entityTypeIn, final Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    protected abstract CartItemStackHandler<SupplyCartEntity> initInventory();

    @Override
    public @NotNull SlotAccess getSlot(final int slot) {
        ItemStackHandler inventory = this.inventory;
        if (slot >= 0 && slot < inventory.getSlots()) {
            return new SlotAccess() {
                @Override
                public ItemStack get() {
                    return inventory.getStackInSlot(slot);
                }

                @Override
                public boolean set(final ItemStack stack) {
                    inventory.setStackInSlot(slot, stack);
                    return true;
                }
            };
        }
        return super.getSlot(slot);
    }

    @Override
    public void onDestroyedAndDoDrops(final DamageSource source) {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.inventory.getStackInSlot(i));
            itementity.setDefaultPickUpDelay();
            this.level().addFreshEntity(itementity);
        }
    }

    @Override
    protected void readAdditionalSaveData(final CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.inventory.deserializeNBT(compound.getCompound("Items"));
    }

    @Override
    protected void addAdditionalSaveData(final CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Items", this.inventory.serializeNBT());
    }


    @Override
    public void remove(final RemovalReason reason) {
        super.remove(reason);
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
            this.itemHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
        if (this.isAlive() && capability == ITEM_HANDLER_CAPABILITY && this.itemHandler != null)
            return this.itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    public abstract double getPassengersRidingOffset();
}
