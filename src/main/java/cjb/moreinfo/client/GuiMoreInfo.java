package cjb.moreinfo.client;


import net.minecraft.client.gui.GuiButton;
import cjb.api.client.GuiModSettings;
import cjb.api.client.GuiMods;
import cjb.moreinfo.common.MoreInfoMod;
import cjb.api.common.Option;

public class GuiMoreInfo extends GuiModSettings {

	private static final Option[] relevantOptions = new Option[] {
		MoreInfoMod.MODE, MoreInfoMod.BIGSIZE, MoreInfoMod.POSITION, MoreInfoMod.CLASSICPOSITION, MoreInfoMod.DEBUFFS, MoreInfoMod.VILLAGE, MoreInfoMod.ITEMINFO, MoreInfoMod.ARROWS, MoreInfoMod.DAYTIME, 
		MoreInfoMod.LIGHTLEVEL, MoreInfoMod.BIOME, MoreInfoMod.SLIMECHUNK, MoreInfoMod.FPS, MoreInfoMod.WEATHER, MoreInfoMod.XP, MoreInfoMod.COORDS,
		MoreInfoMod.H12, MoreInfoMod.MOBHEALTH, MoreInfoMod.BLOCKINFO, MoreInfoMod.TOOLTIPPOSITION, 
		MoreInfoMod.ARMORHEAD, MoreInfoMod.ARMORCHEST, MoreInfoMod.ARMORLEGS, MoreInfoMod.ARMORFEET};
	
	public GuiMoreInfo(String modName) {
		super(modName, "MoreInfo", new GuiMods(), relevantOptions);
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
    }
	
	@Override
	public void initGui() {
		super.initGui();
	}
}
