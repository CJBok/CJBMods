package cjb.mobfilter.client;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cjb.mobfilter.common.FilterProxyCommon;

public class FilterProxyClient extends FilterProxyCommon {
	public void init() {
		super.init();
		//NetworkRegistry.instance().registerChannel(new FilterPacketClient(), "FilterUpdateStr", Side.CLIENT);
	}
}
