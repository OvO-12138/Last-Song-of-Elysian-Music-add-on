package com.he.lastsongofelysian.dimension;

import com.he.lastsongofelysian.lastsongofelysian;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mod.EventBusSubscriber(
        modid = lastsongofelysian.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class ElysianRealmWorldImporter {

    private static final Logger LOGGER =
            LogUtils.getLogger();

    private static final String
            WORLD_ARCHIVE_RESOURCE =
            "/data/lastsongofelysian/"
                    + "elysian_realm/"
                    + "elysian_realm_world.zip";

    private ElysianRealmWorldImporter() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerAboutToStart(
            ServerAboutToStartEvent event
    ) {

        try (
                InputStream resource =
                        ElysianRealmWorldImporter
                                .class
                                .getResourceAsStream(
                                        WORLD_ARCHIVE_RESOURCE
                                )
        ) {
            if (resource == null) {
                LOGGER.warn(
                        "未找到往世乐土结构资源：{}",
                        WORLD_ARCHIVE_RESOURCE
                );
                return;
            }

            Path worldRoot =
                    event.getServer()
                            .getWorldPath(
                                    LevelResource.ROOT
                            );

            Path dimensionRoot =
                    worldRoot.resolve(
                                    "dimensions"
                            )
                            .resolve(
                                    lastsongofelysian.MODID
                            )
                            .resolve(
                                    "elysian_realm"
                            )
                            .normalize();

            Files.createDirectories(
                    dimensionRoot
            );

            deleteRecursively(
                    dimensionRoot.resolve(
                            "region"
                    )
            );

            deleteRecursively(
                    dimensionRoot.resolve(
                            "entities"
                    )
            );

            deleteRecursively(
                    dimensionRoot.resolve(
                            "poi"
                    )
            );

            Files.deleteIfExists(
                    dimensionRoot.resolve(
                            ".elysian_realm_imported_v1"
                    )
            );

            extractArchive(
                    resource,
                    dimensionRoot
            );

            LOGGER.info(
                    "已刷新往世乐土结构：{}",
                    dimensionRoot
            );
        } catch (IOException exception) {
            LOGGER.error(
                    "刷新往世乐土结构失败",
                    exception
            );
        }
    }

    private static void deleteRecursively(
            Path path
    ) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        try (
                Stream<Path> stream =
                        Files.walk(path)
        ) {
            for (
                    Path entry :
                    stream.sorted(
                            Comparator.reverseOrder()
                    ).toList()
            ) {
                Files.deleteIfExists(entry);
            }
        }
    }

    private static void extractArchive(
            InputStream resource,
            Path dimensionRoot
    ) throws IOException {
        Path normalizedRoot =
                dimensionRoot
                        .toAbsolutePath()
                        .normalize();

        try (
                ZipInputStream input =
                        new ZipInputStream(
                                new BufferedInputStream(
                                        resource
                                )
                        )
        ) {
            ZipEntry entry;

            while (
                    (entry = input.getNextEntry())
                            != null
            ) {
                Path output =
                        normalizedRoot.resolve(
                                entry.getName()
                        ).normalize();

                if (
                        !output.startsWith(
                                normalizedRoot
                        )
                ) {
                    throw new IOException(
                            "非法结构资源路径："
                                    + entry.getName()
                    );
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(
                            output
                    );
                } else {
                    Path parent =
                            output.getParent();

                    if (parent != null) {
                        Files.createDirectories(
                                parent
                        );
                    }

                    Files.copy(
                            input,
                            output,
                            StandardCopyOption
                                    .REPLACE_EXISTING
                    );
                }

                input.closeEntry();
            }
        }
    }
}
