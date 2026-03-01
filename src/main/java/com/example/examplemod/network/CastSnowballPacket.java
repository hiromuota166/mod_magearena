package com.example.examplemod.network;

import com.example.examplemod.item.SnowItem;
import com.example.examplemod.magic.MagicHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CastSnowballPacket {
    public CastSnowballPacket() {}

    public static void encode(CastSnowballPacket msg, FriendlyByteBuf buffer) {}

    public static CastSnowballPacket decode(FriendlyByteBuf buffer) {
        return new CastSnowballPacket();
    }

    public static void handle(CastSnowballPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // サーバー側で実行される処理
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                MagicHelper.castSnowball(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}