package cjb.api.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import cjb.api.common.ServerProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
	
	@Override
	public File getConfigDir(boolean startup) {
		if (Minecraft.getMinecraft().isSingleplayer() || startup)
			return new File(Minecraft.getMinecraft().mcDataDir, "/config/cjb/");
		else {
			return new File(Minecraft.getMinecraft().mcDataDir, "/config/cjbmultiplayer/");
		}
	}
	
	@Override
	public void initProxy() {
		FMLCommonHandler.instance().bus().register(new APIKeys());
	}
}
