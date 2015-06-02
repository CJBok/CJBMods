package cjb.xray.client;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import cjb.api.CJBAPI;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class XRayKeys {
	
	public static KeyBinding xrayKey = new KeyBinding("Toggle X-Ray", Keyboard.KEY_X, "CJB Mods");
	public static KeyBinding caveKey = new KeyBinding("Toggle CaveView", Keyboard.KEY_Z, "CJB Mods");
	public static KeyBinding entityKey = new KeyBinding("Toggle Entity X-Ray", Keyboard.KEY_B, "CJB Mods");
	public static KeyBinding nightvisionKey = new KeyBinding("Toggle NightVisionView", Keyboard.KEY_N, "CJB Mods");
	
	public XRayKeys () {
		ClientRegistry.registerKeyBinding(xrayKey);
		ClientRegistry.registerKeyBinding(caveKey);
		ClientRegistry.registerKeyBinding(entityKey);
		ClientRegistry.registerKeyBinding(nightvisionKey);
	}

	@SubscribeEvent
	public void onKeyEvent(InputEvent event) {
		if (CJBAPI.mods.size() > 0 && Minecraft.getMinecraft().currentScreen == null) {			
			if (xrayKey.isPressed()) {
				XRayMod.setMode(1);
			}
			if (caveKey.isPressed()) {
				XRayMod.setMode(2);
			}
			if (entityKey.isPressed()) {
				XRayMod.setMode(3);
			}
			if (nightvisionKey.isPressed()) {
				XRayMod.nvisioneffect = !XRayMod.nvisioneffect;
			}
		}
	}
}
