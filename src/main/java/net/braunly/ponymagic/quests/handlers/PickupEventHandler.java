package net.braunly.ponymagic.quests.handlers;

import me.braunly.ponymagic.api.PonyMagicAPI;
import me.braunly.ponymagic.api.enums.EnumQuestGoalType;
import me.braunly.ponymagic.api.interfaces.IPlayerDataStorage;
import net.braunly.ponymagic.util.QuestGoalUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PickupEventHandler {
    public PickupEventHandler() {
        // stub
    }

    @SubscribeEvent(priority=EventPriority.NORMAL)
    public void onPickUpItem(PlayerEvent.ItemPickupEvent event) {
        EntityPlayer player = event.player;

        if (player.world.isRemote) return;

        IPlayerDataStorage playerData = PonyMagicAPI.playerDataController.getPlayerData(player);
        ItemStack itemStack = event.getStack();
        String goalConfigKey = QuestGoalUtils.getConfigKey(
                EnumQuestGoalType.ITEM,
                itemStack.getItem().getRegistryName(),
                itemStack.getItemDamage()
        );

        String questName = "pickup_item";
        for (int i = 0; i < event.getStack().getCount(); i++) {
            playerData.getLevelData().decreaseGoal(questName, goalConfigKey);
        }

        PonyMagicAPI.playerDataController.savePlayerData(playerData);
    }
}
