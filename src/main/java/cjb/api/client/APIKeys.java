package cjb.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cjb.api.CJBAPI;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;


public class APIKeys {
	
	public static KeyBinding menukey = new KeyBinding("CJB Mods " + I18n.format("cjb.api.keys.menu"), Keyboard.KEY_I, "CJB Mods");

	public APIKeys() {
		ClientRegistry.registerKeyBinding(menukey);
	}

	@SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent event) {
		if (menukey.isPressed() && CJBAPI.mods.size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMods());
		}
	}
}
