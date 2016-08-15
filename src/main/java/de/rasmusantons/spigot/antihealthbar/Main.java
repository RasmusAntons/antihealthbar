package de.rasmusantons.spigot.antihealthbar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

	private ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGH, PacketType.Play.Server.ENTITY_METADATA) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				Player player = event.getPlayer();
				StructureModifier<Entity> modifier = packet.getEntityModifier(event);
				Entity entity = modifier.read(0);
				if (entity == null || !(entity instanceof LivingEntity)
					|| player.equals(entity) || entity instanceof EnderDragon
					|| entity instanceof Wither || player.equals(entity.getPassenger()))
					return;
				packet = packet.deepClone();
				event.setPacket(packet);
				StructureModifier<List<WrappedWatchableObject>> watcher = event.getPacket().getWatchableCollectionModifier();
				for (WrappedWatchableObject watch : watcher.read(0)) {
					if (watch.getIndex() == 7 && (Float) watch.getValue() > 0) {
						watch.setValue((float) ((LivingEntity) entity).getMaxHealth());
						break;
					}
				}
			}
		});
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onMount(final VehicleEnterEvent event) {
		if (event.getEntered() instanceof Player) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					if (event.getVehicle().isValid() && event.getEntered().isValid())
						ProtocolLibrary.getProtocolManager().updateEntity(event.getVehicle(), Collections.singletonList((Player) event.getEntered()));
				}
			});
		}
	}
}
