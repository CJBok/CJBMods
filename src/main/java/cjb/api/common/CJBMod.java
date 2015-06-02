package cjb.api.common;

import java.util.List;

import cjb.api.common.Option;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public abstract class CJBMod {
	
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	@SideOnly(Side.CLIENT)
	public List<String> getInfo(List<String> info) {
		info.add("No information available");
		return info;
	}
	
	public abstract KeyBinding[] getKeys();
	
	public abstract String getName();
	
	public abstract Option[] getOptions();
	
	@SideOnly(Side.CLIENT)
	public abstract GuiScreen getSettingsGui();
	
	public abstract void load();

}
