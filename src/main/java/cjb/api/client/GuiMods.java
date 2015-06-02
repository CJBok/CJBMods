package cjb.api.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import cjb.api.CJBAPI;
import cjb.api.common.CJBMod;

public class GuiMods extends GuiScreen {
	
	private int cx = 0;
	private int cy = 0;
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
    {
		if (par1GuiButton.id >= 0 && par1GuiButton.id < CJBAPI.mods.size()) {
			CJBMod mod = CJBAPI.mods.get(par1GuiButton.id);
			this.mc.displayGuiScreen(mod.getSettingsGui());
			return;
		}
		
		if (par1GuiButton.id >= 100 && par1GuiButton.id < 100 + CJBAPI.mods.size()) {
			CJBMod mod = CJBAPI.mods.get(par1GuiButton.id-100);
			List<String> info = new ArrayList();
			this.mc.displayGuiScreen(new GuiModInfo(this, mod.getInfo(info)));
			return;
		}
		
        switch (par1GuiButton.id) {
            case 1000:
                this.mc.displayGuiScreen(null);
                break;
        }
    }
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void drawScreen(int mx, int my, float f) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, "CJB Mods " + I18n.format("cjb.api.gui.modsettings.title"), cx, 5, 0xFFFFFF);
		super.drawScreen(mx, my, f);
	}
	
	@Override
	public void initGui() {
		
		Keyboard.enableRepeatEvents(false);
		cx = width / 2;
		cy = height / 2;
		
		int col = width < 460 ? 2 : 3;
		
		for (int i = 0 ; i < CJBAPI.mods.size() ; ++i) {
			
			String s = CJBAPI.mods.get(i).getName();
			
			if (s == "") {
				continue;
			}
			
			this.buttonList.add(new GuiButton(i, cx - 130 + ((i % 2 == 1) ? 130 : 0), 30 + 22 * (i >> 1), 100, 20, s));
			this.buttonList.add(new GuiButton(i+100,cx - 30 + ((i % 2 == 1) ? 130 : 0), 30 + 22 * (i >> 1), 20, 20, "?"));
			
		}
		
		if (this.buttonList.isEmpty()) {
			mc.displayGuiScreen(null);
			return;
		}
        
		this.buttonList.add(new GuiButton(1000, cx - 50 , height - 25, 100, 20, I18n.format("gui.done")));
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
    }

}
