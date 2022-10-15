package dev.kezuk.flodoria.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;

public class ParticleAPI {

    private PacketPlayOutWorldParticles packet;

    public ParticleAPI(String type, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
        float x = (float) loc.getX();
        float y = (float) loc.getY();
        float z = (float) loc.getZ();

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, x, y, z, xOffset, yOffset, zOffset, speed, count);
        this.packet = packet;
    }

    public void sendToAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(this.packet);
        }
    }

    public void sendToPlayer(Player p) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(this.packet);
    }

}