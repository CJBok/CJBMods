package cjb.mobfilter.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cjb.mobfilter.MobFilter;
import cjb.mobfilter.common.MobFilterMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;

public class PacketMobFilter implements IMessage {
	
	public static class Handler implements IMessageHandler<PacketMobFilter, IMessage> {

		@Override
		public IMessage onMessage(PacketMobFilter packet, MessageContext message) {
			if (packet.getChannel().equalsIgnoreCase("CJBMF|UpdateEnt")) {
				try {
					DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.data));
					String id = input.readUTF();
		                
					if (MobFilterMod.filterlist.containsKey(id)) {
						MobFilterMod.filterlist.remove(id);
					} else {
		            	MobFilterMod.filterlist.put(id, id);
					}
		          	MobFilterMod.saveFilterList();
		          	
		          	if (message.side == Side.SERVER) {
		          		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    	 	DataOutputStream output = new DataOutputStream(baos);
			    	 	output.writeUTF(id);
			    	  	MobFilter.network.sendToAll(new PacketMobFilter("CJBMF|UpdateEnt", baos.toByteArray()));
		          	}
	            } catch (Exception e) { e.printStackTrace(); }
			}
			
			if (packet.getChannel().equalsIgnoreCase("CJBMF|FitlerList")) {
				try {
	                DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.data));
	                String s = input.readUTF();
	                MobFilterMod.filterlistoption.setString(s);
	                MobFilterMod.loadFilterList();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}
			return null;
		}
	}
	private String channel;
	
	private byte[] data;
	
	public PacketMobFilter(){}
	
	public PacketMobFilter(String channel, byte[] data) {
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
