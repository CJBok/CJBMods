package cjb.cheats.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cjb.api.CJB;
import cjb.api.CJBAPI;
import cjb.cheats.common.CheatProxyCommon;
import cjb.cheats.common.CheatsMod;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class CheatKeys {
	
	public static KeyBinding keyBlockBreakSpeed = new KeyBinding("Block Break Speed Toggle", Keyboard.KEY_LBRACKET, "CJB Mods");
	public static KeyBinding keyItemPickup = new KeyBinding("Item Pick Up Distance Toggle", Keyboard.KEY_RBRACKET, "CJB Mods");
	
	public CheatKeys () {
		ClientRegistry.registerKeyBinding(keyBlockBreakSpeed);
		ClientRegistry.registerKeyBinding(keyItemPickup);
	}
	
	@SubscribeEvent
	public void onKeyEvent(InputEvent event) {
		if (CJBAPI.mods.size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
			if (keyBlockBreakSpeed.isPressed()) {
				CJB.ToggleOption(CheatProxyCommon.BLOCKBREAKSPEED);
				Minecraft.getMinecraft().thePlayer.sendChatMessage(CheatProxyCommon.BLOCKBREAKSPEED.getEnumString());
			}
			if (keyItemPickup.isPressed()) {
				CJB.ToggleOption(CheatProxyCommon.PICKUP);
				Minecraft.getMinecraft().thePlayer.sendChatMessage(CheatProxyCommon.PICKUP.getEnumString());
			}
		}
	}
}
