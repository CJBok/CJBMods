package cjb.mobfilter.client;

import cjb.api.client.GuiButtonOption;
import cjb.mobfilter.common.EntityData;

public class GuiButtonMob extends GuiButtonOption {
	
	public EntityData entdata = null;
	
	public GuiButtonMob(int par1, int par2, int par3, String par4Str, boolean enabled, EntityData entdata) {
		super(par1, par2, par3, par4Str, enabled);
		this.entdata = entdata;
	}
}
