package de.mennomax.astikorcarts.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.inventory.container.CartContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public final class PlowScreen extends AbstractContainerScreen<CartContainer> {
    private static final ResourceLocation PLOW_GUI_BG = ResourceLocation.fromNamespaceAndPath(AstikorCarts.ID, "textures/gui/plow.png");

    public PlowScreen(final CartContainer screenContainer, final Inventory inv, final Component titleIn) {
        super(screenContainer, inv, titleIn);
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.blit(PLOW_GUI_BG, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {

    }
}