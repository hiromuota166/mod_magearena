package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // アイテム登録用のレジスター
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    // 火の杖
    public static final RegistryObject<Item> FIRE_WAND = ITEMS.register("fire_wand",
            () -> new FireWandItem(new Item.Properties()));

    // 雪の杖
    public static final RegistryObject<Item> BONE_WAND = ITEMS.register("bone_wand",
            () -> new BoneItem(new Item.Properties()));

    // 登録処理
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}