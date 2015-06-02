package cjb.moreinfo;

import java.util.ArrayList;
import java.util.List;

import cjb.moreinfo.common.InfoProxyServer;
import cjb.moreinfo.network.PacketMoreInfoClient;
import cjb.moreinfo.network.PacketMoreInfoClient.Handler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="CJB|MoreInfo", name="CJB MoreInfo", version="2.0.0.1", dependencies="required-after:CJBAPI@[2.0.0.0,]")
public class MoreInfo {
	
	public static List<String> debuffs = new ArrayList();
	public static List<String> village = new ArrayList();
	public static String daytime = "";
	public static String lightlevel = "";
	public static String biome = "";
	public static String arrows = "";
	public static String iteminfo = "";
	public static String slimechunk = "";
	public static String weather = "";
	public static String xp = "";
	public static String fps = "";
	public static String coords = "";
	public static List<String> toggles = new ArrayList();
	
	public static long worldTime = 0;
	public static int counter = 0;
	
	public static boolean isRaining = false;
	public static boolean isThundering = false;
	public static int rainTime = 0;
	public static int thunderTime = 0;
	
	public static boolean villageinfo = false;
	public static int villagevillagers = 0;
	public static int villagedoors = 0;
	public static int villagesize = 0;
	public static int villagereputation = 0;
	
	@SidedProxy(clientSide="cjb.moreinfo.client.InfoProxyClient", serverSide="cjb.moreinfo.common.InfoProxyServer")
	public static InfoProxyServer proxy;
	
	public static boolean tconstruct;
	public static boolean thaumcraft;
	
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		ModMetadata meta = Loader.instance().activeModContainer().getMetadata();
		meta.authorList.add("CJB");
		meta.parent = "CJBAPI";
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel("CJBMIPACKETS");
		network.registerMessage(Handler.class, PacketMoreInfoClient.class, 0, Side.CLIENT);
		
		try {
			tconstruct = Class.forName("tconstruct.library.tools.ToolCore") != null;
		} catch(Throwable e) {}
		try {
			thaumcraft = Class.forName("thaumcraft.common.Thaumcraft") != null;
			thaumcraft = Class.forName("thaumcraft.common.items.wands.ItemWandCasting") != null;
		} catch(Throwable e) {}
		proxy.initProxy();
	}
}
