package cjb.teleport;

import cjb.teleport.command.CommandDelHome;
import cjb.teleport.command.CommandHome;
import cjb.teleport.command.CommandSetHome;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;


@Mod(modid="CJB|SetHomes", name="CJB SetHomes", version="1.7.10.0")
public class SetHomes {
	
	@Instance("CJB|SetHomes")
	public static SetHomes instance;
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		ModMetadata meta = Loader.instance().activeModContainer().getMetadata();
		meta.authorList.add("CJB");
		meta.parent = "CJBAPI";
	}
	
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent evt) {
		evt.registerServerCommand(new CommandHome());
		evt.registerServerCommand(new CommandSetHome());
		evt.registerServerCommand(new CommandDelHome());
    }
}
