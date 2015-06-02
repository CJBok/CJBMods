package cjb.xray.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockData {
	private Block block;
	private int x,y,z;
	private boolean fullRender = true;
	
	public BlockData(Block block, int x, int y, int z, boolean fullRender) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fullRender = fullRender;
	}
	
	public void RenderBlock(RenderBlocks renderer) {
		if (fullRender)
			renderer.renderBlockAllFaces(block, x, y, z);
		else
			renderer.renderBlockByRenderType(block, x, y, z);
	}
}
