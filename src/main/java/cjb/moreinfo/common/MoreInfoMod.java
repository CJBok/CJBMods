package cjb.moreinfo.common;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cjb.api.client.APIKeys;
import cjb.api.common.CJBMod;
import cjb.moreinfo.client.GuiMoreInfo;
import cjb.moreinfo.client.InfoKeys;
import cjb.api.common.Option;

public class MoreInfoMod extends CJBMod {
	
	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float var7 = (par5 >> 24 & 255) / 255.0F;
        float var8 = (par5 >> 16 & 255) / 255.0F;
        float var9 = (par5 >> 8 & 255) / 255.0F;
        float var10 = (par5 & 255) / 255.0F;
        float var11 = (par6 >> 24 & 255) / 255.0F;
        float var12 = (par6 >> 16 & 255) / 255.0F;
        float var13 = (par6 >> 8 & 255) / 255.0F;
        float var14 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3, par2, 0);
        var15.addVertex(par1, par2, 0);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4, 0);
        var15.addVertex(par3, par4, 0);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
	
	private static String mod = MoreInfoMod.class.getSimpleName();
	public static Option MODE = new Option(mod, "", false, "viewmode", false, new String[] {"cjb.options.viewmode.default", "cjb.options.viewmode.classic"});
	public static Option BIGSIZE = new Option(mod, "", false, "biggertextsize", false);
	public static Option DAYTIME = new Option(mod, "", true, "showdaytime", false);
	public static Option H12 = new Option(mod, "", false, "show12hclock", false);
	public static Option LIGHTLEVEL = new Option(mod, "", true, "showlightlevel", false);
	public static Option BIOME = new Option(mod, "", true, "showbiome", false);
	public static Option FPS = new Option(mod, "", true, "showfps", false);
	public static Option XP = new Option(mod, "", true, "showxp", false);
	public static Option ARROWS = new Option(mod, "", true, "showarrows", false);
	public static Option ITEMINFO = new Option(mod, "", true, "showiteminfo", false);
	public static Option COORDS = new Option(mod, "", true, "showcoords", false);
	public static Option DEBUFFS = new Option(mod, "", true, "showdebuffs", false);
	public static Option VILLAGE = new Option(mod, "", true, "showvillageinfo", false);
	public static Option SLIMECHUNK = new Option(mod, "", true, "showslimechunk", false);
	public static Option WEATHER = new Option(mod, "", true, "showweather", false);
	public static Option MOBHEALTH = new Option(mod, "", true, "showmobhealth", false);
	public static Option ARMORHEAD = new Option(mod, "", true, "showarmorhead", false);
	public static Option ARMORCHEST = new Option(mod, "", true, "showarmorchest", false);
	public static Option ARMORLEGS = new Option(mod, "", true, "showarmorlegs", false);
	public static Option ARMORFEET = new Option(mod, "", true, "showarmorfeet", false);
	public static Option POSITION = new Option(mod, "", 1, "infoposition", false, new String[]{"cjb.options.windowposition.topleft", "cjb.options.windowposition.topcenter", "cjb.options.windowposition.topright"});
	
	public static Option CLASSICPOSITION = new Option(mod, "", 0, "classicposition", false, new String[]{"cjb.options.windowposition.topleft", "cjb.options.windowposition.topright", "cjb.options.windowposition.bottomleft", "cjb.options.windowposition.bottomright"});
	public static Option BLOCKINFO = new Option(mod, "", true, "showblocktooltip", false);
	
	public static Option TOOLTIPPOSITION = new Option(mod, "", 1, "tooltipposition", false, new String[]{"cjb.options.windowposition.topleft", "cjb.options.windowposition.topright", "cjb.options.windowposition.bottomleft", "cjb.options.windowposition.bottomright"});
	
	public static boolean renderSpawnAreas = false;
	public static boolean flymodinstalled = false;
	
	public static boolean xraymodinstalled = false;
	
	@Override
	public List<String> getInfo(List<String> info) {
		info.add("#lMore Info Mod#r\n\n");
		info.add("This mod adds more information to your HUD.\n\n\n");
		info.add("Press #l" + Keyboard.getKeyName(InfoKeys.spawnskey.getKeyCode()) + "#r to show the lightlevel of nearby blocks \n\n\n");
		return info;
	}
	
	@Override
	public KeyBinding[] getKeys() {
		return new KeyBinding[] { APIKeys.menukey, InfoKeys.spawnskey };
	}
	
	@Override
	public String getName() {
		return "More Info";
	}
	
	@Override
	public Option[] getOptions() {
		return new Option[]{};
	}
	
	@Override
	public GuiScreen getSettingsGui() {
		return new GuiMoreInfo(getClassName());
	}

	@Override
	public void load() {
	}
}
