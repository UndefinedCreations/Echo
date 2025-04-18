package com.undefinedcreations.echo

import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer
import org.bukkit.entity.Player

class NMS1_21_5 : NMS {
    override fun run(player: Player) {
        val serverPlayer = (player as CraftPlayer).handle
        val overworld = MinecraftServer.getServer().overworld()
        val sharedSpawnPos = overworld.sharedSpawnPos
        serverPlayer.sendSystemMessage(Component.literal(sharedSpawnPos.asLong().toString()))
    }
}