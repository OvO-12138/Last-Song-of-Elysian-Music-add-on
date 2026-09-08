package com.he.lastsongofelysian.entity;

import com.he.lastsongofelysian.menu.SpiralWorkshopMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ElysianHeroNpcEntity extends PathfinderMob {

    public enum HeroVariant {
        APONIA,
        ELYSIA,
        GRISEO,
        VILL_V,
        EDEN
    }

    private final HeroVariant variant;

    public ElysianHeroNpcEntity(
            EntityType<? extends ElysianHeroNpcEntity> type,
            Level level,
            HeroVariant variant
    ) {
        super(type, level);
        this.variant = variant;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public HeroVariant getVariant() {
        return this.variant;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.variant != HeroVariant.VILL_V) {
            return super.mobInteract(player, hand);
        }

        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (containerId, inventory, menuPlayer) ->
                                    new SpiralWorkshopMenu(containerId, inventory),
                            Component.literal("螺旋工坊")
                    )
            );
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
