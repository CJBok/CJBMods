package cjb.moreinfo.common;

import cpw.mods.fml.common.FMLCommonHandler;

public class InfoProxyServer {

	public void initProxy() {
		FMLCommonHandler.instance().bus().register(new ProxyTickServer());
	}
}
