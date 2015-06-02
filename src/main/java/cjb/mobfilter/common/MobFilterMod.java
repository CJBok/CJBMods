package cjb.mobfilter.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cjb.api.common.CJBMod;
import cjb.api.common.Option;
import cjb.mobfilter.client.GuiMobFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;


public class MobFilterMod extends CJBMod {
	
	public static Option filterlistoption = new Option(MobFilterMod.class.getSimpleName(), "Filter List", "", "filterlist", true);
	public static Map<String,String> filterlist = new HashMap<String,String>();
	
	public void load() {
		loadFilterList();
	}
	
	public String getName() {
		return "Mob Filter";
	}
	
	public Option[] getOptions() {
		return new Option[]{};
	}
	
	@SideOnly(Side.CLIENT)
	public GuiScreen getSettingsGui() {
		return new GuiMobFilter("Mob Filter", Minecraft.getMinecraft().currentScreen);
	}
	
	@SideOnly(Side.CLIENT)
	public List<String> getInfo(List<String> info) {
		info.add("No information available");
		return info;
	}
	
	public static void loadFilterList() {
		filterlist.clear();
		if (!filterlistoption.getString().contains(","))
			return;
		
		String[] ss = filterlistoption.getString().split(",");
		for (String s : ss) {
				//int i = Integer.parseInt(s);
				filterlist.put(s, s);
		}
	}
	
	public static void saveFilterList() {
		String s = "";
		Iterator it = filterlist.values().iterator();
		
		while(it.hasNext()) {
			s += it.next() + ",";
		}
		
		filterlistoption.setString(s);
	}

	@Override
	public KeyBinding[] getKeys() {
		return null;
	}
}
