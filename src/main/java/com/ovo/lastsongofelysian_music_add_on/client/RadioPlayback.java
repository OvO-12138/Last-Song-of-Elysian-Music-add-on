package com.ovo.lastsongofelysian_music_add_on.client;

import com.ovo.lastsongofelysian_music_add_on.util.RadioSongs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RadioPlayback {
    public enum Mode { LOOP_ALL, SHUFFLE, LOOP_ONE }

    private static RadioSoundInstance currentSound;
    private static volatile String currentSong;
    private static volatile float volume = 1.0f;
    private static final Random random = new Random();
    private static final List<String> playlist = new ArrayList<>();
    private static int sortOrderIndex = 0;
    private static Mode currentMode = Mode.LOOP_ALL;
    private static int builtInStartGraceTicks;

    private static long openAlDevice;
    private static long openAlContext;
    private static volatile boolean customPlaying = false;
    private static volatile int openAlSource = 0;
    private static volatile long activeGeneration;

    private RadioPlayback() {}

    public static void setCurrentMode(Mode m) {
        currentMode = m;
        refreshPlaylist();
    }

    public static Mode getCurrentMode() {
        return currentMode;
    }

    public static void refreshPlaylist() {
        String cs = getCurrentSong();
        if (currentMode == Mode.SHUFFLE) Collections.shuffle(playlist, random);
        if (!playlist.isEmpty()) {
            int idx = cs != null ? playlist.indexOf(cs) : 0;
            sortOrderIndex = idx >= 0 ? idx : 0;
        } else {
            sortOrderIndex = 0;
        }
    }

    public static void setPlaylist(List<String> songs) {
        String cs = getCurrentSong();
        playlist.clear();
        for (String song : songs) {
            if (song != null && !song.isBlank() && !playlist.contains(song)) playlist.add(song);
        }
        if (currentMode == Mode.SHUFFLE) Collections.shuffle(playlist, random);
        int idx = cs == null ? 0 : playlist.indexOf(cs);
        sortOrderIndex = idx >= 0 ? idx : 0;
    }

    public static void play(String songId) {
        play(songId, true);
    }

    public static void play(String songId, boolean updateOrder) {
        if (updateOrder) {
            refreshPlaylist();
            int selectedIndex = playlist.indexOf(songId);
            if (selectedIndex >= 0) sortOrderIndex = selectedIndex;
        }
        if (CustomSongManager.isCustom(songId)) {
            playCustom(songId);
            return;
        }
        
        SoundEvent event = RadioSongs.sound(songId);
        if (event == null) return;
        
        stop();
        currentSound = new RadioSoundInstance(event, volume);
        currentSong = songId;
        builtInStartGraceTicks = 10;
        Minecraft.getInstance().getSoundManager().play(currentSound);
    }

    public static void stop() {
        if (customPlaying) {
            if (openAlSource != 0) {
                try { AL10.alSourceStop(openAlSource); } catch (Exception ignored) {}
            }
            customPlaying = false;
            openAlSource = 0;
            activeGeneration++;
        }
        
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound);
            currentSound = null;
        }
        currentSong = null;
        builtInStartGraceTicks = 0;
    }

    public static boolean isPlaying(String songId) {
        return songId != null && currentSong != null && songId.equals(currentSong);
    }

    public static void tick() {
        if (currentSound == null || currentSong == null || customPlaying) return;
        if (builtInStartGraceTicks > 0) {
            builtInStartGraceTicks--;
            return;
        }
        if (!Minecraft.getInstance().getSoundManager().isActive(currentSound)) {
            currentSound = null;
            autoPlayNext();
        }
    }

    private static void autoPlayNext() {
        if (currentMode == Mode.LOOP_ONE) {
            if (currentSong != null) {
                play(currentSong, false);
            }
        } else {
            int nextIdx = sortOrderIndex + 1;
            if (nextIdx < playlist.size()) {
                sortOrderIndex = nextIdx;
                play(playlist.get(nextIdx), false);
            } else {
                sortOrderIndex = 0;
                if (currentMode == Mode.SHUFFLE) Collections.shuffle(playlist, random);
                if (!playlist.isEmpty()) play(playlist.get(0), false);
            }
        }
    }

    private static void playCustom(String songId) {
        Path path = CustomSongManager.audioPath(songId);
        if (path == null) return;
        
        stop();
        currentSong = songId;
        customPlaying = true;
        long gen = ++activeGeneration;
        
        Minecraft.getInstance().execute(() -> {
            try { Thread.sleep(50L); } catch (InterruptedException e) {}
            
            new Thread(() -> {
                long device = 0L;
                long context = 0L;
                ShortBuffer pcm = null;
                int source = 0;
                int buffer = 0;
                
                boolean completedNaturally = false;
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    IntBuffer channels = stack.mallocInt(1);
                    IntBuffer sampleRate = stack.mallocInt(1);
                    pcm = STBVorbis.stb_vorbis_decode_filename(path.toString(), channels, sampleRate);
                    
                    if (pcm == null || (channels.get(0) != 1 && channels.get(0) != 2)) return;
                    
                    device = ALC10.alcOpenDevice((java.nio.ByteBuffer) null);
                    if (device == 0L) return;
                    
                    ALCCapabilities caps = ALC.createCapabilities(device);
                    context = ALC10.alcCreateContext(device, (IntBuffer) null);
                    if (context == 0L || !ALC10.alcMakeContextCurrent(context)) return;
                    
                    buffer = AL10.alGenBuffers();
                    source = AL10.alGenSources();
                    int format = channels.get(0) == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
                    
                    AL10.alBufferData(buffer, format, pcm, sampleRate.get(0));
                    AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
                    AL10.alSourcef(source, AL10.AL_GAIN, volume);
                    AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
                    AL10.alSourcePlay(source);
                    
                    openAlSource = source;
                    openAlDevice = device;
                    openAlContext = context;
                    
                    while (gen == activeGeneration && currentSong != null) {
                        AL10.alSourcef(source, AL10.AL_GAIN, volume);
                        int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
                        if (state != AL10.AL_PLAYING) {
                            completedNaturally = true;
                            break;
                        }
                        Thread.sleep(50L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    boolean shouldAdvance = completedNaturally && gen == activeGeneration && currentSong != null;
                    cleanup(source, buffer, context, device, pcm, gen);
                    if (shouldAdvance) Minecraft.getInstance().execute(RadioPlayback::autoPlayNext);
                }
            }, "Elysian-Music-Player").start();
        });
    }

    private static void cleanup(int source, int buffer, long context, long device, ShortBuffer pcm, long generation) {
        if (generation == activeGeneration) customPlaying = false;
        if (openAlSource == source && generation == activeGeneration) {
            openAlSource = 0;
        }
        if (openAlDevice == device && generation == activeGeneration) {
            openAlDevice = 0L;
            openAlContext = 0L;
        }
        
        if (source != 0) {
            try { AL10.alSourceStop(source); AL10.alDeleteSources(source); } catch (Exception ignored) {}
        }
        if (buffer != 0) {
            try { AL10.alDeleteBuffers(buffer); } catch (Exception ignored) {}
        }
        if (context != 0L) {
            try {
                ALC10.alcMakeContextCurrent(0L);
                ALC10.alcDestroyContext(context);
            } catch (Exception ignored) {}
        }
        if (device != 0L) {
            try { ALC10.alcCloseDevice(device); } catch (Exception ignored) {}
        }
        if (pcm != null) MemoryUtil.memFree(pcm);
    }

    public static String getCurrentSong() { return currentSong; }
    public static float getVolume() { return volume; }
    public static void setVolume(float vol) {
        volume = Math.max(0.0f, Math.min(1.0f, vol));
        if (currentSound != null) currentSound.setRadioVolume(volume);
    }

    private static final class RadioSoundInstance extends AbstractTickableSoundInstance {
        private RadioSoundInstance(SoundEvent sound, float volume) {
            super(sound, SoundSource.RECORDS, RandomSource.create());
            this.volume = volume;
            this.pitch = 1.0F;
            this.relative = true;
            this.attenuation = Attenuation.NONE;
        }

        private void setRadioVolume(float volume) {
            this.volume = volume;
        }

        @Override
        public void tick() {
        }
    }
}
