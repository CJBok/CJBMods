package cjb.moreinfo.client;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cjb.moreinfo.common.MoreInfoMod;

public class InfoTab {
	
	public static InfoTab[] list = new InfoTab[20];
	public static InfoTab[] listClassic = new InfoTab[20];
	
	private Minecraft mc = Minecraft.getMinecraft();
	private int id = 0;
	private ItemStack itemstack = null;
	private List<String> text = new ArrayList();
	private String tooltip = "";
	private boolean isMultiLine = false;
	
	public int w = 18;
	public int h = 16;
	
	public static InfoTab debuffs = new InfoTab(0, 10, Items.potionitem, "cjb.moreinfo.tab.potioneffects");
	public static InfoTab village = new InfoTab(1, 11, Items.wooden_door, "cjb.moreinfo.tab.village");
	public static InfoTab iteminfo = new InfoTab(2, 1, Items.diamond_pickaxe, "cjb.moreinfo.tab.iteminfo");
	public static InfoTab arrows = new InfoTab(3, 2, Items.arrow, "cjb.moreinfo.tab.arrows");
	public static InfoTab daytime = new InfoTab(4, 3, Items.clock, "cjb.moreinfo.tab.time");
	public static InfoTab lightlevel = new InfoTab(6, 4, Blocks.torch, "cjb.moreinfo.tab.lightlevel");
	public static InfoTab biome = new InfoTab(7, 5, Blocks.sapling, "cjb.moreinfo.tab.biome");
	public static InfoTab slimechunk = new InfoTab(8, 6, Items.slime_ball, "cjb.moreinfo.tab.slimechunk");
	public static InfoTab fps = new InfoTab(9, 7, Items.redstone, "cjb.moreinfo.tab.fps");
	public static InfoTab weather = new InfoTab(10, 8, Blocks.water, "cjb.moreinfo.tab.weather");
	public static InfoTab xp = new InfoTab(11, 9, Items.experience_bottle, "cjb.moreinfo.tab.exp");
	public static InfoTab armor = new InfoTab(12,12, Items.diamond_chestplate, "cjb.moreinfo.tab.armor");
	
	public static InfoTab coords = new InfoTab(15, 0, Items.compass, "cjb.moreinfo.tab.coords");
	public static InfoTab toggles = new InfoTab(16, 16, Items.feather, "cjb.moreinfo.tab.toggles");
	
	
	public InfoTab(int id, int cid, Block item, String tooltip) {
		this.id = id;
		this.itemstack = new ItemStack(item);
		this.tooltip = tooltip;
		list[id] = this;
		listClassic[cid] = this;
	}
	
	public InfoTab(int id, int cid, Item item, String tooltip) {
		this.id = id;
		this.itemstack = new ItemStack(item);
		this.tooltip = tooltip;
		list[id] = this;
		listClassic[cid] = this;
	}
	
	public void drawScreen(int x, int y) {
		
		if (text.isEmpty() || text.get(0)  == "")
			return;
		
        GL11.glColor3f(1, 1, 1);
		
        w = 0;
        w += getWidth()-7;
        
        h = 16;
        
        if (text.size() > 1)
        {
            h += 2 + (text.size() - 1) * 10;
        }
      
        
        if (!MoreInfoMod.MODE.isTrue()) { 
	        int var10 = -267386864-0x80000000;
	        MoreInfoMod.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, var10, var10);
	        MoreInfoMod.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, var10, var10);
	        MoreInfoMod.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, var10, var10);
	        MoreInfoMod.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, var10, var10);
	        MoreInfoMod.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, var10, var10);
	        int var11 = 1347420415;
	        int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
	        MoreInfoMod.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, var11, var12);
	        MoreInfoMod.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, var11, var12);
	        MoreInfoMod.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, h, var11);
	        MoreInfoMod.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, var12, var12);
	        
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        for (int i = 0 ; i < text.size() ; i++) {
	        	String s = text.get(i);
	        	mc.fontRenderer.drawStringWithShadow(s, x+19, y+5+i*11, 0xFFFFFF);
	        }
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        RenderHelper.enableStandardItemLighting();
	        RenderItem itemRenderer = new RenderItem();
	        itemRenderer.zLevel = 300;
	        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
	        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y);
	        itemRenderer.zLevel = 0;
        } else {
        	GL11.glDisable(GL11.GL_DEPTH_TEST);
        	
        	int pos = MoreInfoMod.CLASSICPOSITION.getInt();
        	
        	if (pos == 0 || pos == 2) {
        		if (!isMultiLine) {
            		String s = "\u00a77" + getTooltip() + " \u00a7F" + text.get(0);
    	        	mc.fontRenderer.drawStringWithShadow(s, x, y, 0xFFFFFF);
            	} else {
            		for (int i = 0 ; i < text.size() ; i++) {
            			String s = "\u00a77" + getTooltip() + " \u00a7F";
        	        	mc.fontRenderer.drawStringWithShadow(s, x, y, 0xFFFFFF);
        	        	s = "- " + text.get(i);
        	        	mc.fontRenderer.drawStringWithShadow(s, x+10, y+10+i*11, 0xFFFFFF);
        	        }
            	}
        	} else {
        		if (!isMultiLine) {
        			int tabwidth = mc.fontRenderer.getStringWidth(getTooltip() + " " + text.get(0));
            		String s = text.get(0) + "\u00a77 " + getTooltip() + "\u00a7F";
    	        	mc.fontRenderer.drawStringWithShadow(s, x - tabwidth, y, 0xFFFFFF);
            	} else {
            		
            		int tabwidth = mc.fontRenderer.getStringWidth(getTooltip() + ": ");
            		for (String s : text) {
            			int i = mc.fontRenderer.getStringWidth(s) + 20;
            			if (i > tabwidth)
            				tabwidth = i;
            		}
            		
            		for (int i = 0 ; i < text.size() ; i++) {
            			String s = "\u00a77" + getTooltip() + "\u00a7F";
        	        	mc.fontRenderer.drawStringWithShadow(s, x-mc.fontRenderer.getStringWidth(s), y, 0xFFFFFF);
        	        	s = text.get(i) + " -";
        	        	mc.fontRenderer.drawStringWithShadow(s, x-10-mc.fontRenderer.getStringWidth(s), y+10+i*11, 0xFFFFFF);
        	        }
            	}
        		
        	}
        	
        	
        	
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
       
        
        
        GL11.glColor4f(1, 1, 1, 1);
        RenderHelper.disableStandardItemLighting();
	}
	
	public int getHeight() {
		
		if (!isMultiLine) {
			return 11;
		}
		
		return 10 + ((text.size()) * 11);
	}
	
	public ItemStack getItem() {
		return this.itemstack;
	}
	
	public String getTooltip(){
		return I18n.format(this.tooltip);
	}
	
	public int getWidth() {
		if (text.isEmpty() || text.get(0) == "")
			return 0;
		
		int width = 0;
		
		for (String s : text) {
			int tempwidth = mc.fontRenderer.getStringWidth(s);
			if (tempwidth > width) {
				width = tempwidth;
			}
		}

		return 18 + width + 8;
	}
	
	public void setText(String s) {
		this.text.clear();
		this.text.add(s);
		this.isMultiLine = false;
	}
	
	public void setTextList(List<String> text) {
		this.text = text;
		this.isMultiLine = true;
	}
	
	public boolean shouldDraw() {
		return !text.isEmpty() && text.get(0) != "";
	}
}
