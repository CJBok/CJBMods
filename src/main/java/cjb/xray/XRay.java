package cjb.xray;

import cjb.xray.common.XRayProxyCommon;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid="CJB|XRay", version="2.0.0.1", name="CJB X-Ray", dependencies="required-after:CJBAPI@[2.0.0.0,]")
public class XRay {
	
	@SidedProxy(clientSide="cjb.xray.client.XRayProxyClient", serverSide="cjb.xray.common.XRayProxyCommon")
	public static XRayProxyCommon proxy;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModMetadata meta = Loader.instance().activeModContainer().getMetadata();
		meta.authorList.add("CJB");
		meta.parent = "CJBAPI";
		proxy.initProxy();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInitProxy();
	}
}
