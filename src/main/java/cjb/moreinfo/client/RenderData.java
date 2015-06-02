package cjb.moreinfo.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class RenderData {
	
	private float bx, by, bz;
	private int x,y,z;
	private int color;
	private Block block;
	
	public RenderData (Block block, int x, int y, int z, int color) {
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
		this.bx = x;
		this.by = y;
		this.bz = z;
		this.color = color;
	}
	
	public RenderData (float x, float y, float z, int color) {
		this.bx = x;
		this.by = y;
		this.bz = z;
		this.color = color;
	}
	
	public void drawArea(Tessellator tes, float x, float y, float z) {
		
		float f = 0.05f;
		float f1 = 0.95f;
		
		tes.setColorRGBA_I(color, 50);
		tes.setBrightness(200);
		tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f1);
	}
	
	public void drawAreaCross(Tessellator tes, float x, float y, float z) {
		
		float f = 0.00f;
		float f1 = 1.00f;
		
		tes.setColorRGBA_I(color, 255);
		tes.setBrightness(200);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f);tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f1);tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f);
	}
	
	public void drawAreaLines(Tessellator tes, float x, float y, float z) {
		
		float f = 0.05f;
		float f1 = 0.95f;
		
		tes.setColorRGBA_I(color, 255);
		tes.setBrightness(200);
		tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f);tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f);tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+0.02F, bz-z+f1);tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f1);tes.addVertex(bx-x+f1, by-y+0.02F, bz-z+f);
	}
	
	public void drawCube(Tessellator tes, float x, float y, float z) {
		
		float f = 0.05f;
		float f1 = 0.95f;

		tes.startDrawingQuads();
		tes.setColorRGBA_I(color, 50);
		tes.setBrightness(200);
		
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		tes.draw();
	}
	
	public void drawCubeLines(Tessellator tes, float x, float y, float z) {
		
		float f = 0.00f;
		float f1 = 1.00f;
		
		tes.startDrawing(GL11.GL_LINES);
		tes.setColorRGBA_I(color, 255);
		tes.setBrightness(200);
		
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f);tes.addVertex(bx-x+f1, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f, bz-z+f);tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		
		tes.addVertex(bx-x+f, by-y+f, bz-z+f1);tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1);tes.addVertex(bx-x+f, by-y+f1, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f);tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f, by-y+f, bz-z+f);tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.draw();
		
		/*tes.addVertex(bx-x+f, by-y+f1, bz-z+f); tes.addVertex(bx-x+f1, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f1); tes.addVertex(bx-x+f, by-y+f, bz-z+f);
		tes.addVertex(bx-x+f1, by-y+f1, bz-z+f); tes.addVertex(bx-x+f, by-y+f, bz-z+f1);
		tes.addVertex(bx-x+f, by-y+f1, bz-z+f1); tes.addVertex(bx-x+f1, by-y+f, bz-z+f);*/
	}
	
	public void drawCubeTex(RenderBlocks renderer) {
		
		float f = 0.05f;
		float f1 = 0.95f;
		
		Tessellator tes = Tessellator.instance;
		tes.setBrightness(200);
		renderer.renderBlockAllFaces(block, x, y, z);
		tes.setBrightness(200);
	}

}
