package cjb.mobfilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cjb.mobfilter.common.FilterProxyCommon;
import cjb.mobfilter.network.PacketMobFilter;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="CJB|MobFilter", version="2.0.0.0", name="Mob Filter", dependencies="required-after:CJBAPI@[2.0.0.0,]")

public class MobFilter {

	@SidedProxy(clientSide="cjb.mobfilter.client.FilterProxyClient", serverSide="cjb.mobfilter.common.FilterProxyCommon")
	public static FilterProxyCommon proxy = new FilterProxyCommon();
	
	public static SimpleNetworkWrapper network;
	private static Logger logger = LogManager.getLogger("CJB Mods");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {		
		network = NetworkRegistry.INSTANCE.newSimpleChannel("CJBMF");
		network.registerMessage(PacketMobFilter.Handler.class, PacketMobFilter.class, 0, Side.CLIENT);
		network.registerMessage(PacketMobFilter.Handler.class, PacketMobFilter.class, 1, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
}
