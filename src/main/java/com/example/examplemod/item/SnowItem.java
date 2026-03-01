package com.example.examplemod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SnowItem extends Item {

    public SnowItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // サーバー側でのみ実行（クライアント側では実行しない）
        if (!level.isClientSide()) {
            // プレイヤーの向いている方向を取得
            Vec3 lookVec = player.getLookAngle();
            Vec3 eyePos = player.getEyePosition();

            // 雪玉を生成
            Snowball snowball = new Snowball(
                    level,
                    player
            );

            // 位置の設定
            snowball.setPos(
                    eyePos.x + lookVec.x * 0.5,
                    eyePos.y + lookVec.y * 0.5 - 0.2,
                    eyePos.z + lookVec.z * 0.5
            );

            // 飛ばす
            snowball.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    1.5F,
                    -0.5F
            );

            // ワールドに雪玉を追加
            level.addFreshEntity(snowball);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    // 雪玉を発射する処理(共通化)
    public static InteractionResultHolder<ItemStack> castSnowball(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            // プレイヤーの向いている方向を取得
            Vec3 lookVec = player.getLookAngle();
            Vec3 eyePos = player.getEyePosition();

            // 雪玉を生成
            Snowball snowball = new Snowball(
                    level,
                    player
            );

            // 位置の設定
            snowball.setPos(
                    eyePos.x + lookVec.x * 0.5,
                    eyePos.y + lookVec.y * 0.5 - 0.2,
                    eyePos.z + lookVec.z * 0.5
            );

            // 飛ばす
            snowball.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    1.5F,
                    -0.5F
            );

            // ワールドに雪玉を追加
            level.addFreshEntity(snowball);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}