package net.braunly.ponymagic.spells;

import com.tmtravlr.potioncore.PotionCoreHelper;

import net.braunly.ponymagic.PonyMagic;
import net.braunly.ponymagic.config.Config;
import net.braunly.ponymagic.entity.player.StaminaPlayer;
import net.braunly.ponymagic.entity.player.StaminaPlayer.StaminaType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpellPotion extends Spell {
	
	private int potionId;
	
	public SpellPotion(String potionName) {
		this.spellName = potionName;
		this.potionId = PotionCoreHelper.potions.get("potion." + potionName.toLowerCase()).getId();
	}
	
	// For vanilla potions
	public SpellPotion(String potionName, Integer potionId) {
		this.spellName = potionName;
		this.potionId = potionId;
	}
	
	@Override
	public boolean cast(EntityPlayer player, Integer level) {
		StaminaPlayer props = StaminaPlayer.get(player);
		Integer conf[] = Config.potions.get(this.spellName + "#" + level);
		if (props.remove(StaminaType.CURRENT, conf[1])) {
			int dur = conf[0] * 20;
			int lvl = conf[2] - 1;
			player.addPotionEffect(new PotionEffect(this.potionId, dur, lvl));
			return true;
		}
		return false;
	}
}