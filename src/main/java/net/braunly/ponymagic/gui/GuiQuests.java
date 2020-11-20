package net.braunly.ponymagic.gui;

import lombok.Getter;
import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.network.packets.RequestPlayerDataPacket;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiQuests {
    private final Minecraft mc;
    private IPlayerDataStorage playerData;
    @Getter
    private static boolean isGuiOpen = false;

    public GuiQuests(Minecraft mc) {
        this.mc = mc;
    }

    public static void openGui() {
        GuiQuests.isGuiOpen = true;
    }

    public static void closeGui() {
        GuiQuests.isGuiOpen = false;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (!GuiQuests.isGuiOpen || event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        if (mc.player.capabilities.isCreativeMode || mc.player.isSpectator())
            return;

        getPlayerData();
        if (this.playerData == null || this.playerData.getLevelData().getCurrentGoals().isEmpty())
            return;

        ScaledResolution resolution = event.getResolution();

        int xPos = 10;
        int yPos = 10;
        int yShift = 0;

        for (Map.Entry<String, HashMap<String, Integer>> questEntry : this.playerData.getLevelData().getCurrentGoals().entrySet()) {
            String questName = I18n.format("quest." + questEntry.getKey() + ".name");
            String questString = I18n.format("gui.quest.quest", questName);

            GlStateManager.pushMatrix();
            float scaleFactor = 2.0F;
            GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
            mc.fontRenderer.drawStringWithShadow("§6§l" + questString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
            yShift += 15;

            for (Map.Entry<String, Integer> goalEntry : questEntry.getValue().entrySet()) {
                String goalString = I18n.format(
                        "gui.quest.goal",
                        QuestGoalUtils.getLocalizedGoalName(goalEntry.getKey()),
                        goalEntry.getValue()
                );

                GlStateManager.pushMatrix();
                GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
                mc.fontRenderer.drawStringWithShadow("§l" + goalString, (int) (xPos / scaleFactor), (int)((yPos + yShift) / scaleFactor), 16777215);
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.popMatrix();
                yShift += 15;
            }
            yShift += 15;
        }
    }

    private void getPlayerData() {
        PonyMagic.channel.sendToServer(new RequestPlayerDataPacket());
        this.playerData = PonyMagicAPI.getPlayerDataStorage(mc.player);
    }
}