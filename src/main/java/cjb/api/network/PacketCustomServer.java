package cjb.api.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import cjb.api.CJBAPI;
import cjb.api.common.CJBMod;
import cjb.api.common.Option;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;

public class PacketCustomServer implements IMessage {
	
	public static class Handler implements IMessageHandler<PacketCustomServer, IMessage> {

		@Override
		public IMessage onMessage(PacketCustomServer packet, MessageContext message) {
			
			boolean isPlayerOp = false;
			if (message.side == Side.SERVER) {
				EntityPlayerMP plr = message.getServerHandler().playerEntity;
				for (String s : FMLServerHandler.instance().getServer().getConfigurationManager().func_152606_n()) {
					if (s.toLowerCase().equalsIgnoreCase(plr.getDisplayName().toLowerCase())) {
						isPlayerOp = true;
						break;
					}
				}
			} else {
				isPlayerOp = true;
			}
			
			if (!isPlayerOp) {
				return null;
			}
		 	
			try {
				
				if (packet.getChannel().equalsIgnoreCase("CJBOptToggle")) {
					DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
					OptionSettingToggle(input.readUTF(), input.readUTF());
				}
				
				if (packet.getChannel().equalsIgnoreCase("CJBOptInt")) {
					DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
					OptionSettingInteger(input.readUTF(), input.readUTF(), input.readInt());
				}
				
				if (packet.getChannel().equalsIgnoreCase("CJBOptStr")) {
					DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
					OptionSettingString(input.readUTF(), input.readUTF(), input.readUTF());
				}
			} catch (Throwable e) { e.printStackTrace(); }
			return null;
		}
		
		public void OptionSettingInteger(String mod, String propertyname, int i) {
			for (CJBMod cjbmod : CJBAPI.mods) {
				for (Option opt : cjbmod.getOptions()) {
					if (opt.getPropertyName().equalsIgnoreCase(propertyname)) {
						opt.setInt(i);
						return;
					}
				}
			}
		}
		
		public void OptionSettingString(String mod, String propertyname, String s) {
			for (CJBMod cjbmod : CJBAPI.mods) {
				for (Option opt : cjbmod.getOptions()) {
					if (opt.getPropertyName().equalsIgnoreCase(propertyname)) {
						opt.setString(s);
						return;
					}
				}
			}
		}
		
		public void OptionSettingToggle(String mod, String propertyname) {
			
			for (CJBMod cjbmod : CJBAPI.mods) {
				if (cjbmod.getClass().getSimpleName().equalsIgnoreCase(mod)) {
					for (Option opt : cjbmod.getOptions()) {
						if (opt.getPropertyName().equalsIgnoreCase(propertyname)) {
							opt.increase();
							return;
						}
					}
					return;
				}
			}
		}
	}
	private String channel;
	
	private byte[] data;
	
	public PacketCustomServer() {}
	
	public PacketCustomServer(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	    
	    if (data.length >= 1048576) {
	      throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
	    }
	  }

	@Override
	public void fromBytes(ByteBuf paramByteBuf) {
		PacketBuffer packetbuffer = new PacketBuffer(paramByteBuf);
		
		try {
			channel = packetbuffer.readStringFromBuffer(20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		data = new byte[packetbuffer.readUnsignedShort()];
		packetbuffer.readBytes(data);
	}
	
	public String getChannel() {
		return channel;
	}
	
	public byte[] getData() {
		return data;
	}
	
	@Override
	public void toBytes(ByteBuf paramByteBuf) {
		PacketBuffer packetbuffer = new PacketBuffer(paramByteBuf);
		
		try {
			packetbuffer.writeStringToBuffer(channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		packetbuffer.writeShort(data.length);
		packetbuffer.writeBytes(data);
	}
}
