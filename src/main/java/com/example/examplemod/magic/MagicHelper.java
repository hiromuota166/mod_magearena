package com.example.examplemod.magic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.Vec3;

public class MagicHelper {
    private static void shoot(ServerPlayer player, Projectile projectile, float speed) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();

        projectile.setPos(
                eyePos.x + lookVec.x * 0.5,
                eyePos.y + lookVec.y * 0.5 - 0.1,
                eyePos.z + lookVec.z * 0.5
        );

        projectile.shootFromRotation(
                player,
                player.getXRot(),
                player.getYRot(),
                0.0F,
                speed,
                0.0F
        );

        player.serverLevel().addFreshEntity(projectile);
    }
    public static void castFireball(ServerPlayer player) {
        Vec3 lookVec = player.getLookAngle();

        LargeFireball fireball = new LargeFireball(player.serverLevel(), player, lookVec.x, lookVec.y, lookVec.z, 1);
        shoot(player, fireball, 1.0F);
    }

    public static void castSnowball(ServerPlayer player) {
        Snowball snowball = new Snowball(player.serverLevel(), player);
        shoot(player, snowball, 1.5F);
    }
}
