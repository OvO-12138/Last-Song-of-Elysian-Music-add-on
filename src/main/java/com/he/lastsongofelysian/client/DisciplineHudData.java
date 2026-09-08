package com.he.lastsongofelysian.client;

public class DisciplineHudData {
    public static int preceptCount = 0;
    public static int pulseTicks = 0;

    public static void setPrecept(int precept) {
        int next = Math.max(0, precept);

        if (next != preceptCount) {
            pulseTicks = 8;
        }

        preceptCount = next;
    }

    public static void clientTick() {
        if (pulseTicks > 0) {
            pulseTicks--;
        }
    }
}
