package com.zuxelus.energycontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.zuxelus.energycontrol.config.ConfigHandler;
import com.zuxelus.energycontrol.crossmod.CrossModLoader;
import com.zuxelus.energycontrol.items.ItemHelper;
//import com.zuxelus.energycontrol.tileentities.ScreenManager;
import com.zuxelus.energycontrol.network.ChannelHandler;
import com.zuxelus.energycontrol.tileentities.ScreenManager;
import com.zuxelus.energycontrol.utils.SoundHelper;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(name = EnergyControl.NAME, modid = EnergyControl.MODID, version = EnergyControl.VERSION, dependencies="required-after:ic2", guiFactory = "com.zuxelus.energycontrol.config.GuiFactory") 
public class EnergyControl {
	public static final String NAME = "Energy Control";
    public static final String MODID = "energycontrol";
    public static final String VERSION = "@VERSION@";
    
	@SidedProxy(clientSide = "com.zuxelus.energycontrol.ClientProxy", serverSide = "com.zuxelus.energycontrol.ServerProxy")
	public static ServerProxy proxy;
	@Instance(MODID)
	public static EnergyControl instance;

	// Mod's creative tab
	public static EnCtrlTab creativeTab = new EnCtrlTab();

	// For logging purposes
	public static Logger logger;
	public static ConfigHandler config;
	
	public ScreenManager screenManager = new ScreenManager();
	
	public List<String> availableAlarms; //on server
	public List<String> serverAllowedAlarms = new ArrayList<String>(); // will be loaded from server

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		// Loads configuration
		proxy.loadConfig(event);
		SoundHelper.importSound();

		// registers channel handler
		ChannelHandler.init();
		
		new ItemHelper();
		proxy.registerModels();
		
		CrossModLoader.preinit();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		// Register event handlers
		proxy.registerEventHandlers();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		CrossModLoader.init();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		CrossModLoader.postinit();
		RecipesNew.addRecipes();
	}
}