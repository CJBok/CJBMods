package cjb.mobfilter.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cjb.api.CJB;
import cjb.api.client.GuiButtonOption;
import cjb.mobfilter.MobFilter;
import cjb.mobfilter.common.EntityData;
import cjb.mobfilter.common.EntityUtil;
import cjb.mobfilter.common.MobFilterMod;
import cjb.mobfilter.common.SpawnData;
import cjb.mobfilter.network.PacketMobFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.src.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;
import net.minecraft.util.StringUtils;

public class GuiMobFilter extends GuiScreen {
	
	private final ResourceLocation rl = new ResourceLocation("cjb", "textures/gui/menu.png");
	
	private final String title;
	private final GuiScreen parentgui;
	
	private int xCenter = 0;
	private int yCenter = 0;
	
	private int guiLeft = 0;
	private int guiTop = 0;
	
	private int xSize = 251;
	private int ySize = 199;
	
	private List<GuiButtonMob> btnOptions = new ArrayList();
	private List<EntityData> entities = new ArrayList();
	
	/** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll = 0.0F;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling = false;
		
	private boolean wasClicking;
	
	private EntityLivingBase renderEntity = null;
	
	public GuiMobFilter(String title, GuiScreen parentgui) {
		this.title = title;
		this.parentgui = parentgui;
	}
	
	
	@Override
	public void initGui() {
		
		Keyboard.enableRepeatEvents(false);
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		xCenter = width / 2;
		yCenter = height / 2;
		
		this.buttonList.add(new GuiButtonOption(998, xCenter - 118 , guiTop + ySize - 35, 50, 12, I18n.format("gui.back"), true));
		this.buttonList.add(new GuiButtonOption(999, xCenter - 118 , guiTop + ySize - 20 , 50, 12, I18n.format("gui.done"), true));
		
		EntityUtil.Init();
		
		this.entities.clear();
		
		for (String mod : EntityUtil.GetUsedMods()) {
			EntityData title = new EntityData();
			title.name = "title";
			title.displayName = mod;
			
			entities.add(title);
			
			for (EntityData ent : EntityUtil.entities) {
				if (ent.mod == mod)
					entities.add(ent);
			}
		}
		
		updateBtnOptions();
	}
	
	public void updateBtnOptions() {
		this.btnOptions.clear();
		
		//this.entityids.add(new SpawnData("AGGRESIVE", "ALL AGGRESIVE MOBS"));
		//this.entityids.add(new SpawnData("PASSIVE", "ALL PASSIVE MOBS"));
		
		int var2 = this.entities.size() - 20;
        int var3 = (int)((double)(currentScroll * (float)var2) + 0.5D);

        if (var3 < 0)
        {
            var3 = 0;
        }

        int i = 0;
        for (int var4 = 0; var4 < 20; ++var4) {
            for (int var5 = 0; var5 < 1; ++var5) {
                int var6 = var5 + (var4 + var3) * 1;

                if (var6 >= 0 && var6 < this.entities.size()) {
                	EntityData entdata = this.entities.get(var6);
                	
                	if (entdata.name == "title") {
                		this.btnOptions.add(new GuiButtonMob(var5, guiLeft + 8, guiTop + 19 + 7 * i++, "\u00a72\u00a7l" + entdata.displayName, false, entdata));
                		continue;
                	}
                	
                	
                	String displayName = "";
                	
                	if (MobFilterMod.filterlist.containsKey(entdata.name))
                		displayName += "\u00a7m";
                	
                	displayName += entdata.displayName;
                		
                	this.btnOptions.add(new GuiButtonMob(var5, guiLeft + 8, guiTop + 19 + 7 * i++, displayName, true, entdata));
                }
            }
        }
	}
	
	public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0 && this.needsScrollBars())
        {
            int var2 = this.entities.size()-20;

            if (var1 > 0)
            {
                var1 = 1;
            }

            if (var1 < 0)
            {
                var1 = -1;
            }

            this.currentScroll = (float)((double)this.currentScroll - (double)var1 / (double)var2);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }
            
            updateBtnOptions();
        }
    }
	
	private boolean needsScrollBars() {
		return this.entities.size() > 19;
	}


	@Override
	protected void actionPerformed(GuiButton btn) {
        switch (btn.id) {
        	case 998:
        		this.mc.displayGuiScreen(parentgui);
        		break;
            case 999:
                this.mc.displayGuiScreen(null);
                break;
        }
    }
	
	protected void actionPerformedFilter(GuiButtonMob btn) {
		
		if (mc.isSingleplayer()) {
			if (MobFilterMod.filterlist.containsKey(btn.entdata.name)) {
	        	MobFilterMod.filterlist.remove(btn.entdata.name);
	        } else {
	        	MobFilterMod.filterlist.put(btn.entdata.name, btn.entdata.name);
	        }
	        MobFilterMod.saveFilterList();
	        
		} else {
			try {
	            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
	            DataOutputStream output = new DataOutputStream(var4);
	            output.writeUTF(StringUtils.stripControlCodes(btn.entdata.name));
	            MobFilter.network.sendToServer(new PacketMobFilter("CJBMF|UpdateEnt", var4.toByteArray()));
	        } catch (Throwable e) {
	            e.printStackTrace();
	        }
		}
		updateBtnOptions();
    }
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
        if (par3 == 0) {
            for (int var4 = 0; var4 < this.btnOptions.size(); ++var4) {
                GuiButtonMob var5 = (GuiButtonMob)this.btnOptions.get(var4);

                if (var5.mousePressed(this.mc, par1, par2)) {
                    this.mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    this.actionPerformedFilter(var5);
                }
                
            }
        }
    }
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
		super.mouseMovedOrUp(par1, par2, par3);
		
    }
	
	@Override
	public void drawScreen(int mx, int my, float f) {
		
		boolean var4 = Mouse.isButtonDown(0);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        int var7 = var5 + 238;
        int var8 = var6 + 19;
        int var9 = var7 + 5;
        int var10 = var8 + 142;

        if (!this.wasClicking && var4 && mx >= var7 && my >= var8 && mx < var9 && my < var10)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!var4)
        {
            this.isScrolling = false;
        }

        this.wasClicking = var4;

        if (this.isScrolling)
        {
            this.currentScroll = ((float)(my - var8) - 7.5F) / ((float)(var10 - var8) - 15.0F);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }

            updateBtnOptions();
        }
        
		this.drawDefaultBackground();
		GL11.glColor4f(1, 1, 1, 1);
		CJB.bindTexture(rl);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(var7, var8 + (int)((float)(var10 - var8 - 17) * this.currentScroll), 251, 0 + (this.needsScrollBars() ? 0 : 15), 5, 15);
		
		for (GuiButtonOption btn : btnOptions) {
			btn.drawButton(mc, mx, my);
		}
		
		this.drawCenteredString(fontRendererObj, title + I18n.format("cjb.api.gui.modsettings.title"), xCenter, guiTop + 6, 0xFFFFFF);
		super.drawScreen(mx, my, f);
		
		if (renderEntity != null) {
			int size = (int)( 40F * Math.max(1 / renderEntity.height, 1 / renderEntity.width));
			renderEntity(guiLeft + xSize - 50, guiTop + ySize-40, Math.min(size,25), (float)(this.guiLeft + xSize - 50) - mx, -10, renderEntity);
		}
	}
	
	public static void renderEntity(int par0, int par1, int par2, float par3, float par4, EntityLivingBase par5EntityLivingBase) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par0, (float)par1, 500.0F);
        GL11.glScalef((float)(-par2), (float)par2, (float)par2);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = par5EntityLivingBase.renderYawOffset;
        float f3 = par5EntityLivingBase.rotationYaw;
        float f4 = par5EntityLivingBase.rotationPitch;
        float f5 = par5EntityLivingBase.prevRotationYawHead;
        float f6 = par5EntityLivingBase.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(par4 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        par5EntityLivingBase.renderYawOffset = (float)Math.atan((double)(par3 / 40.0F)) * 20.0F;
        par5EntityLivingBase.rotationYaw = (float)Math.atan((double)(par3 / 40.0F)) * 40.0F;
        par5EntityLivingBase.rotationPitch = -((float)Math.atan((double)(par4 / 40.0F))) * 20.0F;
        par5EntityLivingBase.rotationYawHead = par5EntityLivingBase.rotationYaw;
        par5EntityLivingBase.prevRotationYawHead = par5EntityLivingBase.rotationYaw;
        GL11.glTranslatef(0.0F, par5EntityLivingBase.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(par5EntityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        BossStatus.statusBarTime = -100;
        par5EntityLivingBase.renderYawOffset = f2;
        par5EntityLivingBase.rotationYaw = f3;
        par5EntityLivingBase.rotationPitch = f4;
        par5EntityLivingBase.prevRotationYawHead = f5;
        par5EntityLivingBase.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
    }
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void updateScreen() {
		boolean mousedOverEntityBtn = false;
		for (GuiButtonMob btn : this.btnOptions) {
			if (btn.func_82252_a() && btn.entdata != null && btn.entdata.name != "title") {
				mousedOverEntityBtn = true;
				if (renderEntity == null || renderEntity.getCommandSenderName() != btn.entdata.id) {
					Entity ent = EntityList.createEntityByName(btn.entdata.name, mc.theWorld);
		        	if (ent != null) {
		        		ent.setPosition(9999, 9999, 9999);
		        		this.renderEntity = (EntityLivingBase) ent;
		        		break;
		        	}
				}
			}
		}
		
		if (!mousedOverEntityBtn) this.renderEntity = null;
		//updateBtnOptions();
	}
}
