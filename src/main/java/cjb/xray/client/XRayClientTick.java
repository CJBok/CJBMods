package cjb.xray.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class XRayClientTick {
	
	private Minecraft mc = FMLClientHandler.instance().getClient();
	private int counter = 0;
	private Chunk chunk = null;

	private boolean canAddChunk(int x, int y, int z) {
		
		for (ChunkRender r : Renderer.chunkList) {
			if (r.xc == x && r.zc == z && r.yc == y)
				return false;
		}
		
		return true;
	}

	
	@SubscribeEvent
	public void tickEnd(ClientTickEvent event) {
		
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer plr = mc.thePlayer;
			World w = mc.theWorld;
			
			if (w == null || plr == null)
				return;
			
			XRayMod.counter++;
			
			boolean isGamePaused = mc.isSingleplayer() && mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame();
			
			if(!isGamePaused)
				updateBlocks(w, plr);
			
		}
	}

	public void updateBlocks(World w, EntityPlayer plr) {
		
		int mode = XRayMod.xraymode;
		if (mode > 0 && mode < 3) {
			//mc.skipRenderWorld = true;
		} else {
			//mc.skipRenderWorld = false;
			Renderer.chunkList.clear();
			chunk = null;
			counter = 0;
			ChunkRender.clearGLids();
			return;
		}
		
		try {
			WorldRenderer[] renderers = (WorldRenderer[])ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, 9);
			
			for (WorldRenderer r : renderers) {
				for (ChunkRender cr : Renderer.chunkList) {
					if (r.posX / 16 == cr.xc && r.posY / 16 == cr.yc && r.posZ / 16 == cr.zc && r.isInFrustum && r.needsUpdate && r.isVisible) {
						cr.needsupdate = true;
					}
				}
			}
			
		} catch (Throwable e){}
		
		Chunk c = w.getChunkFromBlockCoords(MathHelper.floor_double(plr.posX), MathHelper.floor_double(plr.posZ));
		if (c != this.chunk) {
			this.chunk = c;

			for (int i = 0 ; i < Renderer.chunkList.size() ; i ++) {
				ChunkRender r = Renderer.chunkList.get(i);
				if (Math.abs(r.xc - this.chunk.xPosition) > 3 || Math.abs(r.zc - this.chunk.zPosition) > 3) {
					r.removeGLid();
					Renderer.chunkList.remove(i);
					i--;
				}
			}
			
			int height = w.provider.getHeight() / 16;
			for (int y = 0 ; y < height ; y++) {
				for (int x = 0 ; x < 3 ; x++) {
					for (int z = 0 ; z < 3 ; z++) {
						int cy = MathHelper.floor_double(plr.posY) / 16;
						
						if (canAddChunk(c.xPosition+x, y, c.zPosition+z))
							Renderer.chunkList.add(new ChunkRender(w, c.xPosition+x, y, c.zPosition+z));
						
						if (canAddChunk(c.xPosition-x, y, c.zPosition-z))
							Renderer.chunkList.add(new ChunkRender(w, c.xPosition-x, y, c.zPosition-z));
						
						if (canAddChunk(c.xPosition-x, y, c.zPosition+z))
							Renderer.chunkList.add(new ChunkRender(w, c.xPosition-x, y, c.zPosition+z));
						
						if (canAddChunk(c.xPosition+x, y, c.zPosition-z))
							Renderer.chunkList.add(new ChunkRender(w, c.xPosition+x, y, c.zPosition-z));
					}
				}
			}
		}
		
		for (ChunkRender r : Renderer.chunkList) {
			r.updateChunk();
		}
	}
}
