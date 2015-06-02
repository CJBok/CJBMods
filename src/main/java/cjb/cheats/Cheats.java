package cjb.cheats;


import cjb.cheats.common.CheatProxyCommon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;



@Mod(modid="CJB|Cheats", name="CJB Cheats", version="2.0.0.1", dependencies="required-after:CJBAPI@[2.0.0.1,)")
public class Cheats {
	
	@SidedProxy(clientSide="cjb.cheats.client.CheatProxyClient", serverSide="cjb.cheats.common.CheatProxyCommon")
	public static CheatProxyCommon proxy;
	
	public static boolean forgeinstalled = false;
	public static boolean ic2installed = false;
	public static boolean tconstructinstalled = false;
	public static boolean thaumcraftinstalled = false;
	public static boolean hqminstalled = false;
	public static boolean enviromine = false;
	public static boolean aemod = false;
	public static boolean exnihilo = false;

	@Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		ModMetadata meta = Loader.instance().activeModContainer().getMetadata();
		meta.authorList.add("CJB");
		meta.parent = "CJBAPI";
		
		proxy.initProxy();
		
		try {
			forgeinstalled = Class.forName("net.minecraftforge.common.ForgeHooks") != null;
		} catch(Throwable e){
			//e.printStackTrace();
			forgeinstalled = false;
		}
		
		try {
			ic2installed = Class.forName("ic2.api.item.ElectricItem") != null;
			ic2installed = Class.forName("ic2.api.item.IElectricItem") != null;
			ic2installed = Class.forName("ic2.core.item.ItemGradual") != null;
			ic2installed = Class.forName("ic2.core.item.ItemIC2") != null;
		} catch(Throwable e){
			//e.printStackTrace();
			ic2installed = false;
		}
		
		try {
			tconstructinstalled = Class.forName("tconstruct.library.tools.ToolCore") != null;
			tconstructinstalled = Class.forName("tconstruct.library.tools.AbilityHelper") != null;
			tconstructinstalled = Class.forName("tconstruct.library.modifier.IModifyable") != null;
			tconstructinstalled = Class.forName("tconstruct.smeltery.logic.SmelteryLogic") != null;
			tconstructinstalled = Class.forName("mantle.blocks.abstracts.InventoryLogic") != null;
			tconstructinstalled = Class.forName("mantle.blocks.iface.IActiveLogic") != null;
			tconstructinstalled = Class.forName("mantle.blocks.iface.IFacingLogic") != null;
			tconstructinstalled = Class.forName("mantle.blocks.iface.IMasterLogic") != null;
			
		} catch(Throwable e) {
			//e.printStackTrace();
			tconstructinstalled = false;
		}
		
		try {
			thaumcraftinstalled = Class.forName("thaumcraft.common.Thaumcraft") != null;
			thaumcraftinstalled = Class.forName("thaumcraft.common.CommonProxy") != null;
			thaumcraftinstalled = Class.forName("thaumcraft.api.aspects.Aspect") != null;
			thaumcraftinstalled = Class.forName("thaumcraft.common.lib.research.PlayerKnowledge") != null;
			thaumcraftinstalled = Class.forName("thaumcraft.common.items.wands.ItemWandCasting") != null;
		} catch(Throwable e) {
			//e.printStackTrace();
			thaumcraftinstalled = false;
		}
		
		try {
			hqminstalled = Class.forName("hardcorequesting.client.interfaces.GuiQuestBook") != null;
		} catch(Throwable e) {
			//e.printStackTrace();
			hqminstalled = false;
		}
		
		try {
			enviromine = Class.forName("enviromine.core.EnviroMine") != null;
		} catch(Throwable e) {
			//e.printStackTrace();
			enviromine = false;
		}
		
		try {
			aemod = Class.forName("appeng.api.implementations.items.IGrowableCrystal") != null;
			aemod = Class.forName("appeng.items.AEBaseItem") != null;
			aemod = Class.forName("appeng.core.features.IAEFeature") != null;
			aemod = Class.forName("appeng.items.misc.ItemCrystalSeed") != null;
		} catch(Throwable e) {
			//e.printStackTrace();
			aemod = false;
		}
		
		try {
			exnihilo = Class.forName("exnihilo.blocks.tileentities.TileEntitySieve") != null;
			exnihilo = Class.forName("exnihilo.blocks.tileentities.TileEntityCrucible") != null;
		} catch(Throwable e) {
			exnihilo = false;
		}
    }
}
