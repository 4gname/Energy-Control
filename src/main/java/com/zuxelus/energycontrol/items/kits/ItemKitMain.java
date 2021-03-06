package com.zuxelus.energycontrol.items.kits;

import java.util.HashMap;
import java.util.Map;

import com.zuxelus.energycontrol.EnergyControl;
import com.zuxelus.energycontrol.api.IItemKit;
import com.zuxelus.energycontrol.crossmod.CrossModLoader;
import com.zuxelus.energycontrol.init.ModItems;
import com.zuxelus.energycontrol.items.cards.ItemCardType;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class ItemKitMain extends Item {
	private static Map<Integer, IItemKit> kits = new HashMap<Integer, IItemKit>();

	public ItemKitMain() {
		super();
		setMaxStackSize(16);
		setHasSubtypes(true);
		setMaxDamage(0);
		canRepair = false;
		setCreativeTab(EnergyControl.creativeTab);
	}

	public final void registerKits() {
		register(new ItemKitEnergy());
		register(new ItemKitCounter());
		register(new ItemKitLiquid());
		register(new ItemKitGenerator());
		if (Loader.isModLoaded("ic2"))
			register(new ItemKitReactor());
		register(new ItemKitLiquidAdvanced());
		register(new ItemKitToggle());
		register(new ItemKitVanilla());
		register(new ItemKitInventory());
		register(new ItemKitRedstone());
		if (Loader.isModLoaded("draconicevolution"))
			register(new ItemKitDraconic());
		if (Loader.isModLoaded("appliedenergistics2"))
			register(new ItemKitAppEng());
		if (Loader.isModLoaded("galacticraftcore") && Loader.isModLoaded("galacticraftplanets"))
			register(new ItemKitGalacticraft());
		if (Loader.isModLoaded("bigreactors"))
			register(new ItemKitBigReactors());
		if (Loader.isModLoaded("nuclearcraft"))
			register(new ItemKitNuclearCraft());
	}

	private void register(ItemKitBase item) {
		if (checkKit(item))
			kits.put(item.getDamage(), item);
	}

	private static boolean checkKit(IItemKit item) {
		if (!kits.containsKey(item.getDamage()))
			return true;
		if (item.getDamage() <= ItemCardType.KIT_MAX)
			EnergyControl.logger.warn(String.format("Kit %s was not registered. ID %d is already used for standard kit.", item.getUnlocalizedName(), item.getDamage()));
		else
			EnergyControl.logger.warn(String.format("Kit %s was not registered. ID %d is already used for extended kit.", item.getUnlocalizedName(), item.getDamage()));
		return false;
	}

	public static final void registerKit(IItemKit item) {
		if (checkKit(item)) {
			if (item.getDamage() <= ItemCardType.KIT_MAX) {
				EnergyControl.logger.warn(String.format("Kit %s was not registered. Kit ID should be bigger than %d", item.getUnlocalizedName(), ItemCardType.CARD_MAX));
				return;
			}
			kits.put(item.getDamage(), item);
		}
	}

	public static final boolean containsKit(int i) {
		return kits.containsKey(i) ? true : false;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int damage = stack.getItemDamage();
		if (kits.containsKey(damage))
			return kits.get(damage).getUnlocalizedName();
		return "";
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab))
			return;
		for (Map.Entry<Integer, IItemKit> entry : kits.entrySet()) {
			Integer key = entry.getKey();
			items.add(new ItemStack(this, 1, key));
		}
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) { 	
		if (player == null || !(player instanceof EntityPlayerMP))
			return EnumActionResult.PASS;
		
		ItemStack stack = player.getHeldItem(hand);
		if (stack.isEmpty() || stack.getCount() != 1)
			return EnumActionResult.PASS;
		ItemStack sensorLocationCard = getItemKitBase(stack.getItemDamage()).getSensorCard(stack, ModItems.itemCard, player, world, pos);
		if (sensorLocationCard.isEmpty())
			return EnumActionResult.PASS;
		
		player.replaceItemInInventory(player.inventory.currentItem, sensorLocationCard);
		return EnumActionResult.SUCCESS;
	}	

	public IItemKit getItemKitBase(int metadata) {
		if (kits.containsKey(metadata))
			return kits.get(metadata);
		return null;
	}

	public static final void registerModels() {
		for (Map.Entry<Integer, IItemKit> entry : kits.entrySet()) {
			Integer key = entry.getKey();
			if (key <= ItemCardType.KIT_MAX)
				ModItems.registerItemModel(ModItems.itemKit, key, kits.get(key).getName());
		}
	}

	public static final void registerExtendedModels() {
		for (Map.Entry<Integer, IItemKit> entry : kits.entrySet()) {
			Integer key = entry.getKey();
			if (key > ItemCardType.KIT_MAX)
				ModItems.registerExternalItemModel(ModItems.itemKit, key, kits.get(key).getName());
		}
	}
}
