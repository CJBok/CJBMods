package cjb.cheats.client;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import cjb.cheats.common.CheatProxyCommon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class CheatProxyClient extends CheatProxyCommon {

	@Override
	public void initProxy() {
		super.initProxy();
		FMLCommonHandler.instance().bus().register(new CheatKeys());
	}
}
