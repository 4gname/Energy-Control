package com.zuxelus.energycontrol.items.cards;

import java.util.List;

import com.zuxelus.energycontrol.api.CardState;
import com.zuxelus.energycontrol.api.ICardGui;
import com.zuxelus.energycontrol.api.ICardReader;
import com.zuxelus.energycontrol.api.PanelSetting;
import com.zuxelus.energycontrol.api.PanelString;
import com.zuxelus.energycontrol.gui.GuiCardText;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCardText extends ItemCardBase {
	public ItemCardText() {
		super(ItemCardType.CARD_TEXT, "card_text");
	}

	@Override
	public CardState update(World world, ICardReader reader, int range, BlockPos pos) {
		return CardState.OK;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<PanelString> getStringData(int displaySettings, ICardReader reader, boolean showLabels) {
		List<PanelString> result = reader.getTitleList();
		boolean started = false;
		for (int i = 9; i >= 0; i--) {
			String text = reader.getString("line_" + i);
			if (text.equals("") && !started)
				continue;
			started = true;
			result.add(0, new PanelString(text));
		}
		return result;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<PanelSetting> getSettingsList(ItemStack stack) {
		return null;
	}
	
	@Override
	public ICardGui getSettingsScreen(ICardReader reader) {
		return new GuiCardText(reader);
	}

	@Override
	public boolean isRemoteCard(int damage) {
		return false;
	}
}
