package com.he.lastsongofelysian.client;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FlawlessBloomClientData {

    private static final Map<UUID, Integer> STACKS =
            new ConcurrentHashMap<>();

    private FlawlessBloomClientData() {
    }

    public static int getStacks(UUID entityUuid) {
        return STACKS.getOrDefault(
                entityUuid,
                0
        );
    }

    public static void setStacks(
            UUID entityUuid,
            int stacks
    ) {
        int safeStacks =
                Math.max(
                        0,
                        Math.min(
                                3,
                                stacks
                        )
                );

        if (safeStacks <= 0) {
            STACKS.remove(entityUuid);
            return;
        }

        STACKS.put(
                entityUuid,
                safeStacks
        );
    }

    public static void clear() {
        STACKS.clear();
    }
}
