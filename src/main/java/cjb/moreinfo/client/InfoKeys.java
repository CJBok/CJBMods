package cjb.moreinfo.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cjb.api.CJBAPI;
import cjb.moreinfo.common.MoreInfoMod;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class InfoKeys {
	
	public static KeyBinding spawnskey = new KeyBinding("Show Light Areas", Keyboard.KEY_L, "CJB Mods");

	public InfoKeys() {
		ClientRegistry.registerKeyBinding(spawnskey);
	}

	@SubscribeEvent
	public void onInputEvent(InputEvent event) {
		
		if (spawnskey.isPressed() && CJBAPI.mods.size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
			MoreInfoMod.renderSpawnAreas = !MoreInfoMod.renderSpawnAreas;
		}
	}
}
