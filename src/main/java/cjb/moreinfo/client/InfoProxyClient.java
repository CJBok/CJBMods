package cjb.moreinfo.client;

import net.minecraftforge.common.MinecraftForge;
import cjb.api.CJBAPI;
import cjb.moreinfo.common.InfoProxyServer;
import cjb.moreinfo.common.MoreInfoMod;
import cpw.mods.fml.common.FMLCommonHandler;

public class InfoProxyClient extends InfoProxyServer {
	
	public static ProxyTickClient tickClient = new ProxyTickClient();

	@Override
	public void initProxy() {
		super.initProxy();
		FMLCommonHandler.instance().bus().register(new ProxyRender());
		FMLCommonHandler.instance().bus().register(new InfoKeys());
		FMLCommonHandler.instance().bus().register(tickClient);
		MinecraftForge.EVENT_BUS.register(new ProxyRender());
		
		CJBAPI.registerCJBmod(MoreInfoMod.class);
		
		try {
			MoreInfoMod.flymodinstalled = Class.forName("cjb.fly.client.FlyMod") != null;
		} catch (Throwable e){
		}
		
		try {
			MoreInfoMod.xraymodinstalled = Class.forName("cjb.xray.client.XRayMod") != null;
		} catch (Throwable e){
		}
		
	}
}
