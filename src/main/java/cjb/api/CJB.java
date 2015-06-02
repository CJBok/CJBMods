package cjb.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import cjb.api.network.PacketCustomServer;
import cjb.api.common.Option;

public class CJB {
	
	public static int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	public static boolean FASTLEAVEDECAY = false;
	public static boolean NOVOID = false;
	
	public static void bindTexture(ResourceLocation rl) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(rl);
	}
	
	public static void ToggleOption(Option opt) {
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.isSingleplayer() && opt.isPacket()) {
			try {
	            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
	            DataOutputStream output = new DataOutputStream(var4);
	            output.writeUTF(opt.getMod());
	            output.writeUTF(opt.getPropertyName());
	            CJBAPI.network.sendToServer(new PacketCustomServer("CJBOptToggle", var4.toByteArray()));
	            opt.increase();
	        } catch (Throwable e) {
	            e.printStackTrace();
	        }
		} else {
			opt.increase();
		}
		
	}
}
