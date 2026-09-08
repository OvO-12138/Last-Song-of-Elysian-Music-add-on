package com.he.lastsongofelysian.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ElysianRealmBlockMutationGuard {

    private static final ThreadLocal<Integer>
            BYPASS_DEPTH =
            ThreadLocal.withInitial(
                    () -> 0
            );

    private ElysianRealmBlockMutationGuard() {
    }

    public static boolean isProtectedLevel(
            Level level
    ) {
        return !level.isClientSide()
                && level.dimension()
                .equals(
                        ModDimensions
                                .ELYSIAN_REALM
                );
    }

    public static boolean shouldRejectChange(
            Level level,
            BlockPos pos,
            BlockState newState
    ) {
        if (
                isBypassing() ||
                !isProtectedLevel(level)
        ) {
            return false;
        }

        BlockState currentState =
                level.getBlockState(pos);

        return currentState.getBlock()
                != newState.getBlock();
    }

    public static boolean shouldRejectRemoval(
            Level level
    ) {
        return !isBypassing()
                && isProtectedLevel(level);
    }

    public static void runWithBypass(
            Runnable action
    ) {
        int previous =
                BYPASS_DEPTH.get();

        BYPASS_DEPTH.set(
                previous + 1
        );

        try {
            action.run();
        } finally {
            if (previous == 0) {
                BYPASS_DEPTH.remove();
            } else {
                BYPASS_DEPTH.set(previous);
            }
        }
    }

    private static boolean isBypassing() {
        return BYPASS_DEPTH.get() > 0;
    }
}
