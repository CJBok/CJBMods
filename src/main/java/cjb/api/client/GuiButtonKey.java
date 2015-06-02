package cjb.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonKey extends GuiButton
{
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean drawButton;
    protected boolean field_82253_i;
    
    public String key = "";
    
    public GuiButtonKey(int par1, int par2, int par3, int par4, int par5, String par6Str, String key) {
    	super(par1, par2, par3, par4, par5, par6Str);
        this.enabled = true;
        this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = par6Str;
        this.key = key;
    }

    public GuiButtonKey(int par1, int par2, int par3, String displayString, String key) {
        this(par1, par2, par3, 228, 7, displayString, key);
    }


    @Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            FontRenderer var4 = par1Minecraft.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int var5 = this.getHoverState(this.field_82253_i);
            
            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF444444);
            Gui.drawRect(xPosition, yPosition, xPosition + width - 1, yPosition + height - 1, 0xFF555555);
            
            this.mouseDragged(par1Minecraft, par2, par3);
            int var6 = 14737632;

            if (this.field_82253_i) {
            	Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x40FFFFFF);
            }
            
            GL11.glPushMatrix();
	    	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), par1Minecraft.displayWidth, par1Minecraft.displayHeight);
	    	GL11.glTranslatef(xPosition+2, yPosition+1, 1);
	    	GL11.glScaled(1.0D / sr.getScaleFactor(), 1.0D / sr.getScaleFactor(), 1.0D);
	    	int scale = sr.getScaleFactor() + 1 >> 1;
	    	GL11.glScalef(scale, scale, 1.0F);
	        this.drawString(var4, this.displayString, 0, 0, var6);
	        
	        GL11.glTranslatef(250, 0, 1);
	        this.drawString(var4, this.key, 0, 0, var6);
            GL11.glPopMatrix();
        }
    }

    public void func_82251_b(int par1, int par2) {}

    public boolean func_82252_a()
    {
        return this.field_82253_i;
    }

    @Override
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {}

    @Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }

    @Override
	public void mouseReleased(int par1, int par2) {}
}
