package com.he.lastsongofelysian.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ElysianRealmChunkBaseline {

    private static final Map<Long, TrackedChunk>
            TRACKED_CHUNKS =
            new LinkedHashMap<>();

    private ElysianRealmChunkBaseline() {
    }

    public static void capture(
            ServerLevel level,
            LevelChunk chunk
    ) {
        long key =
                chunk.getPos().toLong();

        TRACKED_CHUNKS.computeIfAbsent(
                key,
                ignored ->
                        TrackedChunk.capture(
                                level,
                                chunk
                        )
        );
    }

    public static void restoreChangedChunks(
            ServerLevel level
    ) {
        for (
                TrackedChunk tracked :
                TRACKED_CHUNKS.values()
        ) {
            LevelChunk chunk =
                    tracked.chunk;

            if (!chunk.isUnsaved()) {
                continue;
            }

            tracked.restoreDifferences(level);
        }
    }

    public static void restoreAndForget(
            ServerLevel level,
            LevelChunk chunk
    ) {
        TrackedChunk tracked =
                TRACKED_CHUNKS.remove(
                        chunk.getPos().toLong()
                );

        if (tracked != null) {
            tracked.restoreDifferences(level);
        }
    }

    public static void clear() {
        TRACKED_CHUNKS.clear();
    }

    private static final class TrackedChunk {

        private final LevelChunk chunk;

        private final Map<
                Integer,
                Map<Short, BlockState>
                > originalNonAir =
                new HashMap<>();

        private final Map<Long, CompoundTag>
                originalBlockEntities =
                new HashMap<>();

        private TrackedChunk(
                LevelChunk chunk
        ) {
            this.chunk = chunk;
        }

        private static TrackedChunk capture(
                ServerLevel level,
                LevelChunk chunk
        ) {
            TrackedChunk tracked =
                    new TrackedChunk(chunk);

            LevelChunkSection[] sections =
                    chunk.getSections();

            int minSection =
                    level.getMinSection();

            int chunkBaseX =
                    chunk.getPos()
                            .getMinBlockX();

            int chunkBaseZ =
                    chunk.getPos()
                            .getMinBlockZ();

            for (
                    int sectionIndex = 0;
                    sectionIndex < sections.length;
                    sectionIndex++
            ) {
                LevelChunkSection section =
                        sections[sectionIndex];

                if (section.hasOnlyAir()) {
                    continue;
                }

                Map<Short, BlockState> states =
                        new HashMap<>();

                int worldBaseY =
                        (minSection
                                + sectionIndex)
                                << 4;

                for (int localY = 0;
                     localY < 16;
                     localY++) {
                    for (int localZ = 0;
                         localZ < 16;
                         localZ++) {
                        for (int localX = 0;
                             localX < 16;
                             localX++) {
                            BlockState state =
                                    section.getBlockState(
                                            localX,
                                            localY,
                                            localZ
                                    );

                            if (state.isAir()) {
                                continue;
                            }

                            short localKey =
                                    packLocal(
                                            localX,
                                            localY,
                                            localZ
                                    );

                            states.put(
                                    localKey,
                                    state
                            );

                            if (!state.hasBlockEntity()) {
                                continue;
                            }

                            BlockPos pos =
                                    new BlockPos(
                                            chunkBaseX
                                                    + localX,
                                            worldBaseY
                                                    + localY,
                                            chunkBaseZ
                                                    + localZ
                                    );

                            BlockEntity blockEntity =
                                    chunk.getBlockEntity(pos);

                            if (blockEntity != null) {
                                originalBlockEntity(
                                        tracked,
                                        pos,
                                        blockEntity
                                );
                            }
                        }
                    }
                }

                if (!states.isEmpty()) {
                    tracked.originalNonAir.put(
                            sectionIndex,
                            states
                    );
                }
            }

            return tracked;
        }

        private static void originalBlockEntity(
                TrackedChunk tracked,
                BlockPos pos,
                BlockEntity blockEntity
        ) {
            tracked.originalBlockEntities.put(
                    pos.asLong(),
                    blockEntity
                            .saveWithFullMetadata()
                            .copy()
            );
        }

        private void restoreDifferences(
                ServerLevel level
        ) {
            LevelChunkSection[] sections =
                    chunk.getSections();

            int minSection =
                    level.getMinSection();

            int chunkBaseX =
                    chunk.getPos()
                            .getMinBlockX();

            int chunkBaseZ =
                    chunk.getPos()
                            .getMinBlockZ();

            for (
                    int sectionIndex = 0;
                    sectionIndex < sections.length;
                    sectionIndex++
            ) {
                LevelChunkSection section =
                        sections[sectionIndex];

                Map<Short, BlockState> baseline =
                        originalNonAir.get(
                                sectionIndex
                        );

                if (
                        baseline == null &&
                        section.hasOnlyAir()
                ) {
                    continue;
                }

                int worldBaseY =
                        (minSection
                                + sectionIndex)
                                << 4;

                for (int localY = 0;
                     localY < 16;
                     localY++) {
                    for (int localZ = 0;
                         localZ < 16;
                         localZ++) {
                        for (int localX = 0;
                             localX < 16;
                             localX++) {
                            short localKey =
                                    packLocal(
                                            localX,
                                            localY,
                                            localZ
                                    );

                            BlockState expected =
                                    baseline == null
                                            ? Blocks.AIR
                                                    .defaultBlockState()
                                            : baseline.getOrDefault(
                                                    localKey,
                                                    Blocks.AIR
                                                            .defaultBlockState()
                                            );

                            BlockState current =
                                    section.getBlockState(
                                            localX,
                                            localY,
                                            localZ
                                    );

                            if (
                                    current.getBlock()
                                            == expected.getBlock()
                            ) {
                                continue;
                            }

                            BlockPos pos =
                                    new BlockPos(
                                            chunkBaseX
                                                    + localX,
                                            worldBaseY
                                                    + localY,
                                            chunkBaseZ
                                                    + localZ
                                    );

                            restoreBlock(
                                    level,
                                    pos,
                                    current,
                                    expected
                            );
                        }
                    }
                }
            }
        }

        private void restoreBlock(
                ServerLevel level,
                BlockPos pos,
                BlockState current,
                BlockState expected
        ) {
            ElysianRealmBlockMutationGuard
                    .runWithBypass(
                            () -> level.setBlock(
                                    pos,
                                    expected,
                                    Block.UPDATE_CLIENTS
                            )
                    );

            CompoundTag originalNbt =
                    originalBlockEntities.get(
                            pos.asLong()
                    );

            if (
                    originalNbt == null ||
                    !expected.hasBlockEntity()
            ) {
                return;
            }

            BlockEntity restored =
                    level.getBlockEntity(pos);

            if (restored == null) {
                return;
            }

            restored.load(
                    originalNbt.copy()
            );
            restored.setChanged();

            level.sendBlockUpdated(
                    pos,
                    current,
                    expected,
                    Block.UPDATE_CLIENTS
            );
        }

        private static short packLocal(
                int x,
                int y,
                int z
        ) {
            return (short) (
                    (y << 8) |
                    (z << 4) |
                    x
            );
        }
    }
}
