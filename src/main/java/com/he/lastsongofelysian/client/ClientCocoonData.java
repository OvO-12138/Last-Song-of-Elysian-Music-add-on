package com.he.lastsongofelysian.client;

public final class ClientCocoonData {

    private static int coreStage = 0;
    private static boolean reversed;

    private ClientCocoonData() {
    }

    public static int getCoreStage() {
        return coreStage;
    }

    public static void setCoreStage(int stage) {
        coreStage = Math.max(0, Math.min(12, stage));
    }

    public static boolean isReversed() {
        return reversed;
    }

    public static void setReversed(boolean value) {
        reversed = value;
    }

    public static void reset() {
        coreStage = 0;
        reversed = false;
    }
}
