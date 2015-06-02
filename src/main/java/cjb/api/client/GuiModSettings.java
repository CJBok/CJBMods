package cjb.api.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cjb.api.CJB;
import cjb.api.CJBAPI;
import cjb.api.common.Option;

public class GuiModSettings extends GuiScreen {
	
	public Option[] options;
	public String title;
	public GuiScreen parentgui;
	
	public int xCenter = 0;
	public int yCenter = 0;
	
	public int guiLeft = 0;
	public int guiTop = 0;
	
	public int xSize = 251;
	public int ySize = 199;
	
	public List<GuiButtonOption> btnOptions = new ArrayList();
	
	public String modName = "";
	
    private float currentScroll = 0.0F;
    private boolean isScrolling = false;
	private boolean wasClicking;
	
	private final ResourceLocation rl = new ResourceLocation("cjb", "textures/gui/menu.png");
	
	public GuiModSettings(String modName, String title, GuiScreen parentgui, Option[] options) {
		this.modName = modName;
		this.title = title;
		this.options = options;
		this.parentgui = parentgui;
	}
	
	@Override
	protected void actionPerformed(GuiButton btn) {
		
		if (btn.id >= 0 && btn.id < this.options.length) {
			Option opt = options[btn.id];
			CJB.ToggleOption(opt);
			updateBtnOptions();
			return;
		}
		
        switch (btn.id) {
        	case 998:
        		this.mc.displayGuiScreen(parentgui);
        		break;
            case 999:
                this.mc.displayGuiScreen(null);
                break;
            case 1000:
            	this.mc.displayGuiScreen(new GuiModKeys(modName, title, this));
            	break;
        }
    }
	
	@Override
	public boolean doesGuiPauseGame()
    {
        return false;
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
            this.currentScroll = (my - var8 - 7.5F) / (var10 - var8 - 15.0F);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }
        }
        
		this.drawDefaultBackground();
		GL11.glColor4f(1, 1, 1, 1);
		CJB.bindTexture(rl);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		this.drawTexturedModalRect(var7, var8 + (int)((var10 - var8 - 17) * this.currentScroll), 251, (this.needsScrollBars() ? 0 : 15), 5, 15);
		
		this.drawCenteredString(fontRendererObj, title + I18n.format("cjb.api.gui.modsettings.title"), xCenter, guiTop + 6, 0xFFFFFF);
		super.drawScreen(mx, my, f);
		
		for (GuiButtonOption btn : btnOptions) {
			btn.drawButton(mc, mx, my);
		}
	}
	
	@Override
	public void handleMouseInput()
    {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0 && this.needsScrollBars())
        {
            int var2 = options.length - 20/* / 1 - 10 + 1*/;

            if (var1 > 0)
            {
                var1 = 1;
            }

            if (var1 < 0)
            {
                var1 = -1;
            }

            this.currentScroll = (float)(this.currentScroll - (double)var1 / (double)var2);

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

	@Override
	public void initGui() {
		
		Keyboard.enableRepeatEvents(false);
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		xCenter = width / 2;
		yCenter = height / 2;
		
		this.buttonList.add(new GuiButtonOption(998, xCenter - 118 , guiTop + ySize - 35, 50, 12, I18n.format("gui.back"), true));
		this.buttonList.add(new GuiButtonOption(999, xCenter - 118 , guiTop + ySize - 20 , 50, 12, I18n.format("gui.done"), true));
		
		KeyBinding[] keys = CJBAPI.cjbmods.get(modName).getKeys();
		if (keys != null)
			this.buttonList.add(new GuiButtonOption(1000, xCenter + 68 , guiTop + ySize - 20 , 50, 12, I18n.format("options.controls"), true));
		
		updateBtnOptions();
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
    }
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
        if (par3 == 0)
        {
            for (int var4 = 0; var4 < this.btnOptions.size(); ++var4)
            {
                GuiButton var5 = this.btnOptions.get(var4);

                if (var5.mousePressed(this.mc, par1, par2))
                {
                    this.mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    this.actionPerformed(var5);
                }
            }
        }
    }
	
	private boolean needsScrollBars() {
		return options.length > 20;
	}
	
	public void updateBtnOptions() {
		this.btnOptions.clear();
		
		int var2 = this.options.length - 20;
        int var3 = (int)(currentScroll * var2 + 0.50D);

        if (var3 < 0)
        {
            var3 = 0;
        }

        int i = 0;
        for (int var4 = 0; var4 < 20; ++var4) {
            for (int var5 = 0; var5 < 1; ++var5) {
                int var6 = var5 + (var4 + var3) * 1;

                if (var6 >= 0 && var6 < this.options.length) {
                	Option option = this.options[var6];
                	this.btnOptions.add(new GuiButtonOption(var6, guiLeft + 8, guiTop + 19 + 7 * i++, option.getEnumString(), option.enabled));
                }
            }
        }
	}
	
	@Override
	public void updateScreen() {
		updateBtnOptions();
	}
}
