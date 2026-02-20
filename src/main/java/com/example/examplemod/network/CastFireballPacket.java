package com.example.examplemod.network;

import com.example.examplemod.item.FireWandItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CastFireballPacket {
    public CastFireballPacket() {}

    public static void encode(CastFireballPacket msg, FriendlyByteBuf buffer) {}

    public static CastFireballPacket decode(FriendlyByteBuf buffer) {
        return new CastFireballPacket();
    }

    public static void handle(CastFireballPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // サーバー側で実行される処理
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                FireWandItem.castFireball(player.level(), player, InteractionHand.MAIN_HAND);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}