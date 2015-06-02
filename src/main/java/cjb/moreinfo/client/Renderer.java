package cjb.moreinfo.client;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cjb.api.CJB;
import cjb.moreinfo.client.RenderData;
import cjb.moreinfo.common.MoreInfoMod;

public class Renderer{
	
	private Minecraft mc;
	private RenderManager renderManager;
	private int posx,posy,posz;
	
	public static List<RenderData> spawns = new ArrayList<RenderData>();
	public static List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	
	private final ResourceLocation rl = new ResourceLocation("textures/gui/container/inventory.png");
	

	public Renderer(Minecraft mc) {
		this.mc = mc;
	}
	
	private void drawEntitiesHealth(float fx, float fy, float fz, float partialTick) {	
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 0f);
		List<EntityLivingBase> ents = new ArrayList();
		ents.addAll(entities);

		for (EntityLivingBase ent : ents) {
			if (ent == null) continue;
			double d = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * partialTick;
	        double d1 = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * partialTick;
	        double d2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * partialTick;
			renderEntityHealth(ent, d - RenderManager.renderPosX, d1 - RenderManager.renderPosY, d2 - RenderManager.renderPosZ);
		}
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}
	
	private void drawSpawnAreas(float fx, float fy, float fz) {
		
		glDisable(GL_TEXTURE_2D);
		GL11.glLineWidth(0.5f);
		
		List<RenderData> spawnsTemp = new ArrayList();
		spawnsTemp.addAll(spawns);
		
		Tessellator tes = Tessellator.instance;
		
		tes.startDrawing(GL_LINES);
		for (RenderData r : spawnsTemp) {
			if (r != null) r.drawAreaCross(tes, fx, fy, fz);
		}
		tes.draw();
		
		glEnable(GL_TEXTURE_2D);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private String getEntityName(EntityLiving ent) {
		
		if (ent instanceof EntitySkeleton) {
			if (((EntitySkeleton) ent).getSkeletonType() == 1)
				return ent.getCommandSenderName() + " Wither";
		}
		
		return ent.getCommandSenderName();
	}
		
	public void render (float f) {	
		if (mc.theWorld == null || mc.thePlayer == null)
			return;
		
		renderManager = RenderManager.instance;
		
		posx = MathHelper.floor_double(mc.thePlayer.posX);
		posy = MathHelper.floor_double(mc.thePlayer.posY);
		posz = MathHelper.floor_double(mc.thePlayer.posZ);
		
		float px = (float)mc.thePlayer.posX; 
		float py = (float)mc.thePlayer.posY; 
		float pz = (float)mc.thePlayer.posZ;
     	float mx = (float)mc.thePlayer.prevPosX;
		float my = (float)mc.thePlayer.prevPosY;
		float mz = (float)mc.thePlayer.prevPosZ;
     	float x = mx + ( px - mx ) * f;
		float y = my + ( py - my ) * f;
		float z = mz + ( pz - mz ) * f;
		
		if (MoreInfoMod.renderSpawnAreas)
			drawSpawnAreas(x,y,z);
		
		/*if (MoreInfoMod.MOBHEALTH.isTrue())
			drawEntitiesHealth(x,y,z,f);*/
    }
		
	private void renderEntityHealth(EntityLivingBase ent, double par3, double par5, double par7) {
		
		if (ent == null)
			return;
		
    	float fheight = ent.height + 0.5f;
        
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        glPushMatrix();
        glTranslatef((float)par3 + 0.0F, (float)par5 + fheight, (float)par7);
        glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        glScalef(-f2, -f2, f2);
        
        GuiIngame g = mc.ingameGUI;
        
        float health = Math.max(0F, ent.getHealth() / ent.getMaxHealth());
        int r = ((int)(255 * health) & 255) << 16;
       	int gr = ((int)(255 * health) & 255) << 8;
       	
       	Gui.drawRect(-20 + (int)(40 * health), 0, 20, 4, 0x80000000);
	   	Gui.drawRect(-20, 0,-20 + (int)(40 * health), 4, 0x80000000 + gr - r);
	        
	   	if (ent instanceof EntityAnimal) {
	     	if (((EntityAnimal)ent).getGrowingAge() == 0) {
	        	glColor4f(1, 1, 1, 0.8F);
	        	glScalef(0.5f, 0.5f, f2);
	        		
	        	glEnable(GL_BLEND);
	        	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	        		
	        	CJB.bindTexture(rl);
	        	g.drawTexturedModalRect(48, -4, 127, 200, 16, 16);
	        }
	   	}
	   	
	   	if (ent instanceof EntityVillager) {
	     	if (((EntityVillager)ent).isMating()) {
	        	glColor4f(1, 1, 1, 0.8F);
	        	glScalef(0.5f, 0.5f, f2);
	        		
	        	glEnable(GL_BLEND);
	        	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	        		
	        	CJB.bindTexture(rl);
	        	g.drawTexturedModalRect(48, -4, 127, 200, 16, 16);
	        }
	   	}
        
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glPopMatrix();
    }
}
