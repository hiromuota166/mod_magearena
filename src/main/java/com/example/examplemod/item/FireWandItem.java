package com.example.examplemod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireWandItem extends Item {

    public FireWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        castFireball(level, player, hand);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    // 火球を発射する処理（共通化）
    public static void castFireball(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Vec3 lookVec = player.getLookAngle();

            LargeFireball fireball = new LargeFireball(
                    level,
                    player,
                    lookVec.x,
                    lookVec.y,
                    lookVec.z,
                    1
            );

            fireball.setPos(
                    player.getX() + lookVec.x * 2,
                    player.getEyeY(),
                    player.getZ() + lookVec.z * 2
            );

            level.addFreshEntity(fireball);
        }
    }
}