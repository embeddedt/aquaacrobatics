package com.fuzs.aquaacrobatics.client.gui;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URI;

@SideOnly(Side.CLIENT)
public class GuiNoMixin extends GuiScreen {

    private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");

    private final GuiScreen parent;

    public GuiNoMixin(GuiScreen parent) {

        this.parent = parent;
    }

    @Override
    public void initGui() {

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20, new TextComponentString("Go to CurseForge").getFormattedText()));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20, new TextComponentString("Ignore Message").getFormattedText()));
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        switch (button.id)
        {
            case 1:

                button.enabled = false;
                try {

                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI("https://www.curseforge.com/minecraft/mc-mods/mixinbootstrap"));
                } catch (Throwable throwable) {

                    AquaAcrobatics.LOGGER.error("Couldn't open link", throwable);
                }

                break;
            case 2:

                this.mc.displayGuiScreen(this.parent);
                this.mc.setIngameFocus();
        }
    }

    @Override
    public void drawDefaultBackground() {

        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(DEMO_BACKGROUND_LOCATION);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        String title = new TextComponentString(AquaAcrobatics.NAME + " Message").setStyle(new Style().setBold(true)).getFormattedText();
        this.fontRenderer.drawString(title, this.width / 2 - this.fontRenderer.getStringWidth(title) / 2, j, 2039583);
        j += 12;
        this.fontRenderer.drawSplitString(new TextComponentString("WARNING! " + AquaAcrobatics.NAME + " has failed to load. No instance of the Mixin library detected.").getFormattedText(), i, j + 12, 218, 5197647);
        this.fontRenderer.drawSplitString(new TextComponentString("Download the MixinBootstrap mod from CurseForge to enable the Mixin library. Click the button below to head to the downloads page.").getFormattedText(), i, j + 56, 218, 2039583);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
