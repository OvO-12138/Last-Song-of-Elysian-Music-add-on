package com.ovo.lastsongofelysian_music_add_on.item;

import com.ovo.lastsongofelysian_music_add_on.util.RadioSongs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class RadioSongDiscItem extends Item {
    private final String songId;

    public RadioSongDiscItem(String songId, Properties properties) {
        super(properties);
        this.songId = songId;
    }

    public String songId() {
        return songId;
    }

    public MutableComponent songTitle() {
        return RadioSongs.title(songId);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("HOYO-MiX").withStyle(ChatFormatting.GRAY));
    }
}
