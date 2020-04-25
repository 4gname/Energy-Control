package com.zuxelus.energycontrol.items.kits;

import com.zuxelus.energycontrol.crossmod.CrossModLoader;
import com.zuxelus.energycontrol.items.ItemHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKitAppEng extends ItemKitBase {
	public ItemKitAppEng() {
		super(ItemHelper.KIT_APPENG, "kit_app_eng");
	}

	@Override
	public String getUnlocalizedName() {
		return "item.kit_app_eng";
	}

	@Override
	protected ItemStack getSensorCard(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
		return ItemStack.EMPTY;
		//return CrossModLoader.crossAppEng.getSensorCard(stack, world, pos);
	}
}