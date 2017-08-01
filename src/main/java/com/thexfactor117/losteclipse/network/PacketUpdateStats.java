package com.thexfactor117.losteclipse.network;

import com.thexfactor117.losteclipse.capabilities.CapabilityPlayerStats;
import com.thexfactor117.losteclipse.capabilities.api.IStats;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author TheXFactor117
 *
 */
public class PacketUpdateStats implements IMessage
{
	private int mana;
	private int maxMana;
	private int manaPerSecond;
	
	private int healthPerSecond;
	
	private double criticalChance;
	private double criticalDamage;
	
	public PacketUpdateStats() {}
	
	public PacketUpdateStats(IStats statsCap)
	{
		this.mana = statsCap.getMana();
		this.maxMana = statsCap.getMaxMana();
		this.manaPerSecond = statsCap.getManaPerSecond();
		
		this.healthPerSecond = statsCap.getHealthPerSecond();
		
		this.criticalChance = statsCap.getCriticalChance();
		this.criticalDamage = statsCap.getCriticalDamage();
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		mana = buf.readInt();
		maxMana = buf.readInt();
		manaPerSecond = buf.readInt();
		
		healthPerSecond = buf.readInt();
		
		criticalChance = buf.readDouble();
		criticalDamage = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(mana);
		buf.writeInt(maxMana);
		buf.writeInt(manaPerSecond);
		
		buf.writeInt(healthPerSecond);
		
		buf.writeDouble(criticalChance);
		buf.writeDouble(criticalDamage);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateStats, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketUpdateStats message, final MessageContext ctx) 
		{			
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run() 
				{
					EntityPlayer player = Minecraft.getMinecraft().player;
					IStats statsCap = player.getCapability(CapabilityPlayerStats.STATS, null);
					
					if (statsCap != null)
					{
						statsCap.setMana(message.mana);
						statsCap.setMaxMana(message.maxMana);
						statsCap.setManaPerSecond(message.manaPerSecond);
						
						statsCap.setHealthPerSecond(message.healthPerSecond);
						
						statsCap.setCriticalChance(message.criticalChance);
						statsCap.setCriticalDamage(message.criticalDamage);
					}
				}
			});
			
			return null;
		}
	}
}