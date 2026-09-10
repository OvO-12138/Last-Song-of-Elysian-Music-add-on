package com.ovo.lastsongofelysian_music_add_on.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CustomSongManager {
    private static final String PREFIX = "custom:";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type SONG_LIST_TYPE = new TypeToken<List<SavedSong>>() {
    }.getType();
    private static final Path ROOT = FMLPaths.CONFIGDIR.get()
            .resolve("lastsongofelysian_music_add_on")
            .resolve("custom_songs");
    private static final Path INDEX = ROOT.resolve("songs.json");
    private static final Map<String, SavedSong> SONGS = new LinkedHashMap<>();
    private static final Map<String, ResourceLocation> COVERS = new LinkedHashMap<>();
    private static boolean loaded;

    private CustomSongManager() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        SONGS.clear();
        try {
            Files.createDirectories(ROOT);
            if (Files.isRegularFile(INDEX)) {
                String json = Files.readString(INDEX, StandardCharsets.UTF_8);
                List<SavedSong> saved = GSON.fromJson(json, SONG_LIST_TYPE);
                if (saved != null) {
                    for (SavedSong song : saved) {
                        if (song != null && song.id != null && song.name != null
                                && song.audioFile != null && song.coverFile != null
                                && Files.isRegularFile(ROOT.resolve(song.audioFile))
                                && Files.isRegularFile(ROOT.resolve(song.coverFile))) {
                            SONGS.put(song.id, song);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static synchronized String importSong(String name, Path ogg, Path cover) throws IOException {
        load();
        String cleanName = name == null ? "" : name.trim();
        if (cleanName.isEmpty()) {
            throw new IOException("请输入音乐名字");
        }
        if (!Files.isRegularFile(ogg) || !ogg.getFileName().toString().toLowerCase().endsWith(".ogg")) {
            throw new IOException("音乐必须是 .ogg 文件");
        }
        if (!Files.isRegularFile(cover) || !isImage(cover)) {
            throw new IOException("照片必须是 PNG、JPG 或 JPEG");
        }

        Files.createDirectories(ROOT);
        String id = PREFIX + UUID.randomUUID().toString().replace("-", "");
        String key = id.substring(PREFIX.length());
        String coverExtension = extension(cover);
        String audioFile = key + ".ogg";
        String coverFile = key + coverExtension;
        Files.copy(ogg, ROOT.resolve(audioFile), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(cover, ROOT.resolve(coverFile), StandardCopyOption.REPLACE_EXISTING);

        SavedSong song = new SavedSong(id, cleanName, audioFile, coverFile);
        SONGS.put(id, song);
        saveIndex();
        return id;
    }

    public static synchronized List<String> ids() {
        load();
        return Collections.unmodifiableList(new ArrayList<>(SONGS.keySet()));
    }

    public static synchronized boolean isCustom(String songId) {
        load();
        return songId != null && SONGS.containsKey(songId);
    }

    public static synchronized String title(String songId) {
        load();
        SavedSong song = SONGS.get(songId);
        return song == null ? "" : song.name;
    }

    public static synchronized Path audioPath(String songId) {
        load();
        SavedSong song = SONGS.get(songId);
        return song == null ? null : ROOT.resolve(song.audioFile);
    }

    public static synchronized ResourceLocation cover(String songId) {
        load();
        SavedSong song = SONGS.get(songId);
        if (song == null) {
            return null;
        }
        ResourceLocation existing = COVERS.get(songId);
        if (existing != null) {
            return existing;
        }
        try (InputStream stream = Files.newInputStream(ROOT.resolve(song.coverFile))) {
            NativeImage image = NativeImage.read(stream);
            DynamicTexture texture = new DynamicTexture(image);
            ResourceLocation location = Minecraft.getInstance().getTextureManager()
                    .register("lastsongofelysian_music_add_on/custom_song/" + song.id.substring(PREFIX.length()), texture);
            COVERS.put(songId, location);
            return location;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static void saveIndex() throws IOException {
        Files.writeString(INDEX, GSON.toJson(new ArrayList<>(SONGS.values()), SONG_LIST_TYPE),
                StandardCharsets.UTF_8);
    }

    private static boolean isImage(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
    }

    private static String extension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? ".png" : name.substring(dot).toLowerCase();
    }

    private static final class SavedSong {
        private String id;
        private String name;
        private String audioFile;
        private String coverFile;

        private SavedSong(String id, String name, String audioFile, String coverFile) {
            this.id = id;
            this.name = name;
            this.audioFile = audioFile;
            this.coverFile = coverFile;
        }
    }
}
