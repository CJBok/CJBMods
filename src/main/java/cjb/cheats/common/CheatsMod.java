package cjb.cheats.common;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import cjb.api.client.GuiModSettings;
import cjb.api.common.CJBMod;
import cjb.cheats.Cheats;
import cjb.cheats.client.CheatKeys;
import cjb.api.common.Option;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CheatsMod extends CJBMod {
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<String> getInfo(List<String> info) {
		info.add("#lCheats Mod#r\n\n");
		info.add("This mod contains some nifty cheats. Like No Damage, increased Pickup Radius and much more!");
		return info;
	}
	
	@Override
	public KeyBinding[] getKeys() {
		return new KeyBinding[] {CheatKeys.keyBlockBreakSpeed, CheatKeys.keyItemPickup};
	}
	
	@Override
	public String getName() {
		return "Cheats";
	}
	
	@Override
	public Option[] getOptions() {
		
		ArrayList<Option> options = new ArrayList();
		
		options.add(CheatProxyCommon.DEFAULT);
		options.add(CheatProxyCommon.DAMAGE); 
		options.add(CheatProxyCommon.ONEHITKILL); 
		options.add(CheatProxyCommon.FOOD); 
		options.add(CheatProxyCommon.INVISIBLE); 
		options.add(CheatProxyCommon.PICKUP); 
		options.add(CheatProxyCommon.PICKUPORBS); 
		options.add(CheatProxyCommon.FASTFURNACE); 
		options.add(CheatProxyCommon.XP); 
		options.add(CheatProxyCommon.ITEMDAMAGE); 
		options.add(CheatProxyCommon.INFINITEARROWS); 
		options.add(CheatProxyCommon.AUTOJUMP);
		options.add(CheatProxyCommon.TIME);
		options.add(CheatProxyCommon.ADULT);
		options.add(CheatProxyCommon.FASTGROWTH);
		options.add(CheatProxyCommon.BLOCKBREAKSPEED);
		options.add(CheatProxyCommon.NOPICKAXE);
		options.add(CheatProxyCommon.WEATHER);
		options.add(CheatProxyCommon.AIR);
		options.add(CheatProxyCommon.KEEPITEMS);
		options.add(CheatProxyCommon.NOVOIDFOG);
		options.add(CheatProxyCommon.FASTLEAVEDECAY);
		options.add(CheatProxyCommon.REMOVEDEBUFFS);
		
		if(Cheats.ic2installed){
			options.add(CheatProxyCommon.IC2);
			options.add(CheatProxyCommon.IC2ENERGY); 
		}
		if(Cheats.thaumcraftinstalled) {
			options.add(CheatProxyCommon.THAUMCRAFT);
			options.add(CheatProxyCommon.RESEARCHASPECT);
			options.add(CheatProxyCommon.WANDVIS);
		}
		if(Cheats.enviromine){
			options.add(CheatProxyCommon.ENVIROMINE);
			options.add(CheatProxyCommon.CAMELPACK);
		}
		if(Cheats.aemod) {
			options.add(CheatProxyCommon.AE);
			options.add(CheatProxyCommon.CRYSTALGROWTH);
		}
		if(Cheats.tconstructinstalled) {
			options.add(CheatProxyCommon.TC);
			options.add(CheatProxyCommon.INSTANTSMELTSMELTERY);
		}
		if(Cheats.exnihilo) {
			options.add(CheatProxyCommon.EXNIHILO);
			options.add(CheatProxyCommon.INSTANTSIEVE);
			options.add(CheatProxyCommon.FASTERCRUCIBLES);
		}
		
		
		Option[] opts = new Option[options.size()];
		return options.toArray(opts);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getSettingsGui() {
		return new GuiModSettings(getClassName(), getName(), Minecraft.getMinecraft().currentScreen, getOptions());
	}

	@Override
	public void load() {
	}
}
