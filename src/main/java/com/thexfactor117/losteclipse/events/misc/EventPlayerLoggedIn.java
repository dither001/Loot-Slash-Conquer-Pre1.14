package com.thexfactor117.losteclipse.events.misc;

import com.thexfactor117.losteclipse.LostEclipse;
import com.thexfactor117.losteclipse.capabilities.CapabilityPlayerInformation;
import com.thexfactor117.losteclipse.capabilities.IPlayerInformation;
import com.thexfactor117.losteclipse.network.PacketClassGui;
import com.thexfactor117.losteclipse.network.PacketUpdatePlayerInformation;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EventPlayerLoggedIn 
{
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		IPlayerInformation playerInfo = event.player.getCapability(CapabilityPlayerInformation.PLAYER_INFORMATION, null);

		if (playerInfo != null && playerInfo.getPlayerClass() == 0)
			LostEclipse.network.sendTo(new PacketClassGui(), (EntityPlayerMP) event.player);
		else if (playerInfo != null && playerInfo.getPlayerClass() > 0)
			LostEclipse.network.sendTo(new PacketUpdatePlayerInformation(playerInfo), (EntityPlayerMP) event.player);
	}
}