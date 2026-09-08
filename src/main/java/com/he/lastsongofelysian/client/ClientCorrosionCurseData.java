package com.he.lastsongofelysian.client;

public final class ClientCorrosionCurseData {

    private static boolean acquired;
    private static boolean reversed;

    private ClientCorrosionCurseData() {
    }

    public static boolean isAcquired() {
        return acquired;
    }

    public static boolean isReversed() {
        return acquired && reversed;
    }

    public static void set(boolean newAcquired, boolean newReversed) {
        acquired = newAcquired;
        reversed = newAcquired && newReversed;
    }

    public static void reset() {
        acquired = false;
        reversed = false;
    }
}
