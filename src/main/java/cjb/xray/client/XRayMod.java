package cjb.xray.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cjb.api.client.APIKeys;
import cjb.api.common.CJBMod;
import cjb.api.common.Option;


public class XRayMod extends CJBMod {
	
	public static void loadBlackList() {
		String[] ss = blocklist.getString().split(",");
		for (String s : ss) {
			if (s.contains(":")) {
				String[] ssm = s.split(":");
				int id = Integer.parseInt(ssm[0]);
				int meta = Integer.parseInt(ssm[1]);
				blacklist.add(id, meta);
			} else {
				int i = Integer.parseInt(s);
				blacklist.add(i, 0);
			}
		}
	}
	public static void loadRenderers() {
		//mc.renderGlobal.loadRenderers();
	}
	public static void saveBlackList() {
		String s = "";
		for (XData x : blacklist.list) {
			s += x.id + ":" + x.meta + ",";
		}
		
		blocklist.setString(s);
	}
	public static void setMode(int i) {
		
		if (world != mc.theWorld) {
			world = mc.theWorld;
			xraymode = 0;
			nvision = false;
		}
		
		if (xraymode == i) {
			xraymode = 0;
			nvisioneffect = false;
		} else
			xraymode = i;
		
		nvision = xraymode > 0;
		
		loadRenderers();
	}
	private static String mod = XRayMod.class.getSimpleName();
	private static Minecraft mc = Minecraft.getMinecraft();
	public static boolean nvision = false;
	
	public static boolean nvisioneffect = false;
	public static boolean tempnvision = false;
	public static int xraymode = 0;
	public static XData blacklist = new XData();
	
	private static Option blocklist = new Option(mod, "", "1,2,3,4,7,12,13,17,18,24,31,32,78,87,88,106,121","blacklistmeta", false);
	
	private static float[] brightnesstemplate;
	
	private static World world = null;
	
	public static int counter = 0;
	
	@Override
	public List<String> getInfo(List<String> info) {
		info.add("#lX-Ray Mod#r\n\n");
		info.add("This mod gives you the ability to see any type of blocks through walls or see caves\n\n\n");
		info.add("Press #l" + Keyboard.getKeyName(XRayKeys.xrayKey.getKeyCode()) + "#r to enable X-Ray View\n\n");
		info.add("Press #l" + Keyboard.getKeyName(XRayKeys.caveKey.getKeyCode()) + "#r to enable Cave View\n\n");
		info.add("Press #l" + Keyboard.getKeyName(XRayKeys.nightvisionKey.getKeyCode()) + "#r to enable Nightvision View");
		return info;
	}
	
	@Override
	public KeyBinding[] getKeys() {
		return new KeyBinding[] { APIKeys.menukey, XRayKeys.xrayKey, XRayKeys.caveKey, XRayKeys.nightvisionKey, XRayKeys.entityKey };
	}
	
	@Override
	public String getName() {
		return "X-Ray";
	}
	
	@Override
	public Option[] getOptions() {
		return new Option[]{};
	}
	
	@Override
	public GuiScreen getSettingsGui() {
		return new GuiBlacklist(getClassName(), mc.currentScreen);
	}

	@Override
	public void load() {
		loadBlackList();
	}
	
}
