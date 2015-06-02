package cjb.xray.client;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import cjb.api.CJB;
import cpw.mods.fml.client.FMLClientHandler;

public class ChunkRender {
	public static void clearGLids() {
		ChunkRender.GLids.clear();
	}
	private Minecraft mc = FMLClientHandler.instance().getClient();
	private static Map<Integer, Integer> GLids = new HashMap();
	private World w;
	public int xc = 0;
	public int zc = 0;
	public int yc = 0;
	public boolean needsupdate = true;
	public boolean needsrenderupdate = false;
	
	public int GLid = 0;
	
	private List<BlockData> renderList = new ArrayList();
	
	public ChunkRender(World w, int xc, int yc, int zc) {
		this.w = w;
		this.xc = xc;
		this.zc = zc;
		this.yc = yc;
		this.GLid = this.getAvailableID();
	}
	
	private int getAvailableID() {
		int i = 1;
		for (; i < 500 ; i++) {
			if (!ChunkRender.GLids.containsKey(99999 + i)) {
				ChunkRender.GLids.put(99999 + i, 99999 + i);
				return 99999 + i;
			}
		}
		return 99999;
	}
	
	public void removeGLid() {
		ChunkRender.GLids.remove(this.GLid);
	}
	
	public void render(float fx, float fy, float fz, float partialTick) {
		if (needsrenderupdate) {
			needsrenderupdate = false;
			
			Tessellator tes = Tessellator.instance;
			int ambi = mc.gameSettings.ambientOcclusion;
			mc.gameSettings.ambientOcclusion = 0;
			glNewList(GLid, GL_COMPILE);
			
			glPushMatrix();
				tes.startDrawingQuads();
				tes.setBrightness(200);
				CJB.bindTexture(TextureMap.locationBlocksTexture);
				for (BlockData r : renderList) {
					r.RenderBlock(mc.renderGlobal.renderBlocksRg);
				}
				tes.draw();
			glPopMatrix();
			
			glEndList();
			mc.gameSettings.ambientOcclusion = ambi;
		}
	}
	
	public void updateChunk() {
		
		if (needsupdate) {
			needsupdate = false;
			
			renderList.clear();
			
			int mode = XRayMod.xraymode;
			int xb = xc * 16;
			int zb = zc * 16;
			int yb = yc * 16;
			
			for (int y = yb ; y < yb + 16 ; y++) {
				for (int x = xb ; x < xb + 16 ; x++) {
					for (int z = zb ; z < zb + 16 ; z++) {
						int id = Block.getIdFromBlock(w.getBlock(x, y, z));
						
						if (mode == 1 && id > 0 && !XRayMod.blacklist.contains(id, -1)) {
							renderList.add(new BlockData(Block.getBlockById(id), x, y, z, true));
						}
						if (mode == 2 && id > 0 && (Block.getBlockById(id) == Blocks.stone || !XRayMod.blacklist.contains(id, -1))) {
							renderList.add(new BlockData(Block.getBlockById(id), x, y, z, false));
						}
					}
				}
			}
			needsrenderupdate = true;
		}
	}
}
