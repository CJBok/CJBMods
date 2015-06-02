package cjb.xray.client;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import cjb.api.CJBAPI;
import cjb.xray.common.XRayProxyCommon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class XRayProxyClient extends XRayProxyCommon {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private static boolean nvAdded = false;
	
	@Override
	public void initProxy() {
		CJBAPI.registerCJBmod(XRayMod.class);
		FMLCommonHandler.instance().bus().register(new XRayKeys());
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(new XRayClientTick());
	}
	
	@Override
	public void postInitProxy() {
		MinecraftForge.EVENT_BUS.register(new Renderer());
	}

	@SubscribeEvent
	public void tickEnd(RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.END || mc.thePlayer == null)
			return;
		
		if (nvAdded) {
			mc.thePlayer.removePotionEffectClient(Potion.nightVision.getId());
			nvAdded = false;
		}
		
	}

	@SubscribeEvent
	public void tickStart(RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.START || mc.thePlayer == null)
			return;
		
		if (mc.thePlayer.getActivePotionEffect(Potion.nightVision) != null)
			return;
			
		if ( XRayMod.nvisioneffect || XRayMod.xraymode > 0) {
			PotionEffect pe = new PotionEffect(Potion.nightVision.getId(), 10000);
			mc.thePlayer.addPotionEffect(pe);
			nvAdded = true;
		}
	}
}
