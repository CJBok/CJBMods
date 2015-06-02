package cjb.moreinfo.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import cjb.moreinfo.MoreInfo;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketMoreInfoClient implements IMessage {
	
	public static class Handler implements IMessageHandler<PacketMoreInfoClient, IMessage> {

		@Override
		public IMessage onMessage(PacketMoreInfoClient packet, MessageContext message) {
			if (packet.getChannel().equalsIgnoreCase("CJBMI|weather")) {
				try {
	                DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
	                MoreInfo.isRaining = input.readBoolean();
	                MoreInfo.isThundering = input.readBoolean();
	                MoreInfo.rainTime = input.readShort();
	                MoreInfo.thunderTime = input.readShort();
	            } catch (Exception var9) {
	                var9.printStackTrace();
	            }
			}
			
			if (packet.getChannel().equalsIgnoreCase("CJBMI|village")) {
				try {
	                DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
	                MoreInfo.villageinfo = input.readBoolean();
	                MoreInfo.villagevillagers = input.readShort();
	                if (!MoreInfo.villageinfo)
	                	return null;
	                
	                MoreInfo.villagedoors = input.readShort();
	                MoreInfo.villagesize = input.readShort();
	                MoreInfo.villagereputation = input.readShort();
	            } catch (Exception var9) {
	                var9.printStackTrace();
	            }
			}
			
			if (packet.getChannel().equalsIgnoreCase("CJBMI|slime")) {
				try {
	                DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
	                MoreInfo.slimechunk = input.readBoolean() ? "Chunk" : "";
	            } catch (Exception var9) {
	                var9.printStackTrace();
	            }
			}
			
			if (packet.getChannel().equalsIgnoreCase("CJBMI|skylight")) {
				try {
	                DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.getData()));
	                if (Minecraft.getMinecraft().theWorld != null)
	                	Minecraft.getMinecraft().theWorld.skylightSubtracted = input.readInt();
	            } catch (Exception var9) {
	                var9.printStackTrace();
	            }
			}
			return null;
		}
	}
	private String channel;
	
	private byte[] data;
	
	public PacketMoreInfoClient(){}
	
	public PacketMoreInfoClient(String channel, byte[] data) {
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
