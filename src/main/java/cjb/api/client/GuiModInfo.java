package cjb.api.client;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cjb.api.CJB;

public class GuiModInfo extends GuiScreen
{
    /** Whether the book is signed or can still be edited */
    private boolean bookModified;
    private boolean editingTitle;

    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage = 1;
    private final List<String> info;
    private final GuiScreen parentgui;
    private String text = "";
    private List<String> textlist;
    
    private GuiButtonNextPage buttonNextPage;
    private GuiButtonNextPage buttonPreviousPage;
    private final ResourceLocation rl = new ResourceLocation("textures/gui/book.png");

    public GuiModInfo(GuiScreen parent, List<String> info)
    {
       this.info = info;
       this.parentgui = parent;
       currPage = 1;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
	protected void actionPerformed(GuiButton par1GuiButton)
    {
    	
    	if (par1GuiButton.id == 1) {
    		currPage++;
    		if (this.currPage > this.bookTotalPages)
    			this.currPage = this.bookTotalPages;
    	}
    	
    	if (par1GuiButton.id == 2) {
    		currPage--;
    		if (this.currPage < 1)
    			this.currPage = 1;
    	}
    	
    	if (par1GuiButton.id == 1000) {
    		mc.displayGuiScreen(this.parentgui);
    	}
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
    	this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        CJB.bindTexture(rl);
        int w = (this.width - this.bookImageWidth) / 2;
        int h = 2;
        this.drawTexturedModalRect(w, h, 0, 0, this.bookImageWidth, this.bookImageHeight);
        
        int j = 0;
        for (int i = currPage * 16 - 16 ; i < currPage * 16 && i < textlist.size(); ++i) {
        	String s = textlist.get(i);
        	this.fontRendererObj.drawString(s, w + 36,  h + 16 + 9 * j++, 0x000000);
        }
        
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
	public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        
        int cx = width / 2;
        int cy = height / 2;
        
        text = "";
        for (String s : info) {
        	text += s.replace("#", "\u00a7");
        }
        
        this.textlist = this.fontRendererObj.listFormattedStringToWidth(text, 116);
        this.bookTotalPages = textlist.size() / 17 + 1;

        this.buttonList.add(new GuiButton(1000, cx - 50 , 190, 100, 20, I18n.format("gui.done")));
        this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, cx + 20, 160, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, cx - 60, 160, false));
    }


    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
	protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
	public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }


    /**
     * Called from the main game loop to update the screen.
     */
    @Override
	public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
        
        if (this.bookTotalPages == currPage) {
        	this.buttonNextPage.enabled = false;
        	this.buttonNextPage.visible = false;
        } else if (this.bookTotalPages > 1) {
        	this.buttonNextPage.enabled = true;
        	this.buttonNextPage.visible = true;
        } else {
        	this.buttonNextPage.enabled = false;
        	this.buttonNextPage.visible = false;
        }
        
        if (this.currPage > 1) {
        	this.buttonPreviousPage.enabled = true;
        	this.buttonPreviousPage.visible = true;
        } else {
        	this.buttonPreviousPage.enabled = false;
        	this.buttonPreviousPage.visible = false;
        }
    }
}
