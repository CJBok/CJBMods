package cjb.moreinfo.client;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cjb.api.CJB;
import cjb.moreinfo.MoreInfo;
import cjb.moreinfo.common.MoreInfoMod;
import cjb.moreinfo.client.Renderer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ProxyRender {

	private Minecraft mc = Minecraft.getMinecraft();
	private Renderer renderer = new Renderer(mc);
	private boolean newRender = false;
	private boolean classesChecked = false;
	private boolean forestry = false;
	private float partialTicks = 0;
	
	private final ResourceLocation rl = new ResourceLocation("textures/gui/container/inventory.png");
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
			MoreInfo.counter++;
	}
	
	@SubscribeEvent
	public void onRenderEntityLivingBaseSpecial(RenderLivingEvent.Specials.Post event) {
		if (!MoreInfoMod.MOBHEALTH.isTrue()) {
			return;
		}
		
		EntityLivingBase ent = event.entity;
		EntityPlayer plr = mc.thePlayer;
		RenderManager renderManager = RenderManager.instance;
		
		
		double par3 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX)/* * (double)this.partialTicks*/;
        double par5 = ent.lastTickPosY + (ent.posY - ent.lastTickPosY)/* * (double)this.partialTicks*/;
        double par7 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ)/* * (double)this.partialTicks*/;
        
        par3 -= RenderManager.renderPosX;
        par5 -= RenderManager.renderPosY;
        par7 -= RenderManager.renderPosZ;
        		
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        float f2 = 0.016666668F * f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + ent.height + 0.5F, (float)par7);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
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
	   	
	   	GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
	}
	
	@SubscribeEvent
	public void onRenderEvent(RenderWorldLastEvent event) {
		this.partialTicks = event.partialTicks;
		renderer.render(event.partialTicks);
	}
	
	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post e) {
		if (e.type == ElementType.EXPERIENCE) {
			newRender = true;
			if (mc.theWorld == null || mc.thePlayer == null)
				return;
			
			renderInfo();
			if(MoreInfoMod.BLOCKINFO.isTrue())
				renderBlockInfo();
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if (mc.currentScreen == null && !mc.gameSettings.showDebugInfo && !newRender) {
			if (mc.theWorld == null || mc.thePlayer == null)
				return;
				
			renderInfo();
			if(MoreInfoMod.BLOCKINFO.isTrue())
					renderBlockInfo();
		}
	}
	
	public void renderBlockInfo() {
		
		if (!classesChecked) {
			try {
				forestry = Class.forName("forestry.core.config.ForestryBlock") != null;
			} catch (Throwable e) {}
			classesChecked = true;
		}
		
		Map<Integer,Integer> specialblocks = new HashMap();
		
		/*if (forestry && ForestryBlock.resources != null) {
			specialblocks.put(ForestryBlock.resources.blockID, ForestryBlock.resources.blockID);
		}*/
		
		MovingObjectPosition mov = mc.objectMouseOver;
		String blockname = "";
		List<ItemStack> items = new ArrayList();
		boolean dropped = false;
		
		if (mov != null && mov.typeOfHit == MovingObjectType.BLOCK) {
			
			int x = mov.blockX;
			int y = mov.blockY;
			int z = mov.blockZ;
			
			int id = Block.getIdFromBlock(mc.theWorld.getBlock(x, y, z));
			int meta = mc.theWorld.getBlockMetadata(x, y, z);
			
			Block mouseoverBlock = Block.getBlockById(id);
			
			if (items.size() == 0 && !specialblocks.containsValue(id)) {
				ItemStack pick = mouseoverBlock.getPickBlock(mov, mc.theWorld, x, y, z);
				if (pick != null) {
					items.add(0, pick);
				}
			}
			
			if (items.size() == 0) {
				try {
					items.addAll(0, mouseoverBlock.getDrops(mc.theWorld, x, y, z, meta, 0));
					dropped = true;
				} catch (Throwable e) {}
			}
			
			if (items.size() == 0) {
				items.add(0, new ItemStack(mouseoverBlock, 1, mc.theWorld.getBlockMetadata(x, y, z)));
			}
				
			blockname = id + (meta > 0 ? ":" + meta + " " : "") + " " + I18n.format(Block.getBlockById(id).getLocalizedName());
		}
		
		int position = MoreInfoMod.TOOLTIPPOSITION.getInt();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
        List<String> var4 = new ArrayList();
        
        if (!items.isEmpty()) {
	        try {
	        	for (ItemStack stack : items) {
	        		var4.add(Item.getIdFromItem(stack.getItem()) + (stack.getItemDamage() > 0 ? ":" + stack.getItemDamage() : "") + " " + (String) stack.getTooltip(mc.thePlayer, false).get(0));
	        	}
	        } catch (Throwable e) {}
        }
        
        if (var4 == null || var4.isEmpty() || var4.get(0) == "" ) {
        	var4.add(blockname);
        }

        if (!var4.isEmpty() && var4.get(0) != "" ) {
            int var5 = 0;
            int var6;
            int var7;
            
            if (dropped) {
            	for (var6 = 0; var6 < var4.size(); ++var6) {
                    var7 = mc.fontRenderer.getStringWidth(var4.get(var6));

                    if (var7 > var5)
                    {
                        var5 = var7;
                    }
                }
            } else {
            	var5 = mc.fontRenderer.getStringWidth(var4.get(0));
            }

           
            var6 = position == 1 || position == 3 ? sr.getScaledWidth() - var5 - 5 : 5;
            var7 = position > 1 ? sr.getScaledHeight() - 14 : 5;
            int var9 = 8;
            
            if (dropped) {
	            if (var4.size() > 1)
	            {
	                var9 += 2 + (var4.size() - 1) * 10;
	            }
	
	            if (0 + var7 + var9 + 6 > sr.getScaledHeight())
	            {
	                var7 = sr.getScaledHeight() - var9 - 0 - 6;
	            }
            }
            
            int var10 = -267386864;
            MoreInfoMod.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            MoreInfoMod.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            MoreInfoMod.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            MoreInfoMod.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            MoreInfoMod.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            MoreInfoMod.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            MoreInfoMod.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            MoreInfoMod.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            MoreInfoMod.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

            for (int var13 = 0; var13 < (dropped ? var4.size() : 1) ; ++var13)
            {
                String var14 = var4.get(var13);

                mc.fontRenderer.drawStringWithShadow(var14, var6, var7, -1);

                if (var13 == 0)
                {
                    var7 += 2;
                }

                var7 += 10;
            }
        }
	}
	
	public void renderInfo() {
		glPushMatrix();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		if (!MoreInfoMod.MODE.isTrue()) {
			int position = MoreInfoMod.POSITION.getInt();
			int totalwidth = 0;
			
			for (InfoTab tab : InfoTab.list) {
				if (tab != null)
					totalwidth += tab.getWidth();
			}
			
			int x = sr.getScaledWidth() / 2;
			
			if (position == 0) {
				x = 0;
			} else if (position == 2) {
				x = sr.getScaledWidth();
			}
			
			glTranslatef(x, 0, -500);
			glScaled(1.0D / sr.getScaleFactor(), 1.0D / sr.getScaleFactor(), 1.0D);
			int scale = !MoreInfoMod.BIGSIZE.isTrue() ? sr.getScaleFactor() + 1 >> 1 : sr.getScaleFactor();
			GL11.glScalef(scale, scale, 1.0F);
			
			int prevwidth = 0;
			int offset = -totalwidth / 2;
			
			if (position == 0) {
				offset = 0;
			} else if (position == 2) {
				offset = -totalwidth+6;
			}
			
			for (InfoTab tab : InfoTab.list) {
				if (tab != null) {
					tab.drawScreen(prevwidth + offset, 0);
					prevwidth += tab.getWidth();
				}
			}
		} else {
			int pos = MoreInfoMod.CLASSICPOSITION.getInt();
			/*int totalheight = 0;
			
			for (int i = 0 ; i < InfoTab.listClassic.length ; ++i) {
				InfoTab tab = InfoTab.listClassic[i];
				if (tab != null && tab.shouldDraw()) {
					totalheight += tab.getHeight();
				}
			}*/
			
			int x = pos == 0 || pos == 2 ? 2 : sr.getScaledWidth() - 2;
			int y = pos == 0 || pos == 1 ? 2 : sr.getScaledHeight() - 2;
			
			glTranslatef(x, y, 100);
			glScaled(1.0D / sr.getScaleFactor(), 1.0D / sr.getScaleFactor(), 1.0D);
			int scale = !MoreInfoMod.BIGSIZE.isTrue() ? sr.getScaleFactor() + 1 >> 1 : sr.getScaleFactor();
			GL11.glScalef(scale, scale, 1.0F);
			
			int prevheight = 0;
			
			if (pos == 0 || pos == 1) {
				for (int i = 0 ; i < InfoTab.listClassic.length ; ++i) {
					InfoTab tab = InfoTab.listClassic[i];
					if (tab != null && tab.shouldDraw()) {
						tab.drawScreen(0, prevheight);
						prevheight += tab.getHeight();
					}
				}
			} else {
				for (int i = 0 ; i < InfoTab.listClassic.length ; ++i) {
					InfoTab tab = InfoTab.listClassic[i];
					if (tab != null && tab.shouldDraw()) {
						tab.drawScreen(0, prevheight - tab.getHeight());
						prevheight -= tab.getHeight();
					}
				}
			}
		}
		
		glPopMatrix();
	}
}
