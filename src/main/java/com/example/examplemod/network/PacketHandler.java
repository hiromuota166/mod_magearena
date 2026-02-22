package com.example.examplemod.network;

import com.example.examplemod.ExampleMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, CastFireballPacket.class,
                CastFireballPacket::encode,
                CastFireballPacket::decode,
                CastFireballPacket::handle);

        INSTANCE.registerMessage(id++, CastSnowballPacket.class,
                CastSnowballPacket::encode,
                CastSnowballPacket::decode,
                CastSnowballPacket::handle);

        System.out.println("PacketHandler: チャンネル登録完了！");
    }
}