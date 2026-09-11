package com.ovo.lastsongofelysian_music_add_on.menu;

import com.ovo.lastsongofelysian_music_add_on.item.RadioItem;
import com.ovo.lastsongofelysian_music_add_on.item.RadioSongDiscItem;
import com.ovo.lastsongofelysian_music_add_on.registry.ModMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class RadioMenu extends AbstractContainerMenu {
    public static final int BUTTON_RECORD = 0;
    public static final String TAG_TRACKS = "RecordedTracks";
    public static final int MAX_TRACKS = 64;

    private final Player player;
    private final InteractionHand hand;
    private final SimpleContainer discInput = new SimpleContainer(1);

    public RadioMenu(int containerId, Inventory inventory, FriendlyByteBuf buffer) {
        this(containerId, inventory, buffer.readEnum(InteractionHand.class));
    }

    public RadioMenu(int containerId, Inventory inventory, InteractionHand hand) {
        super(ModMenus.RADIO_MENU.get(), containerId);
        this.player = inventory.player;
        this.hand = hand;

        addSlot(new Slot(discInput, 0, 25, 83) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof RadioSongDiscItem;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9,
                        48 + column * 18, 191 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 48 + column * 18, 249));
        }
    }

    public ItemStack radioStack() {
        return player.getItemInHand(hand);
    }

    public ItemStack inputDisc() {
        return discInput.getItem(0);
    }

    public List<String> recordedSongs() {
        List<String> songs = new ArrayList<>();
        ItemStack radio = radioStack();
        if (radio.isEmpty()) {
            return songs;
        }

        CompoundTag tag = radio.getOrCreateTag();
        ListTag list = tag.getList(TAG_TRACKS, Tag.TAG_STRING);
        for (Tag entry : list) {
            songs.add(entry.getAsString());
        }
        return songs;
    }

    public boolean canRecord() {
        ItemStack input = inputDisc();
        if (!(input.getItem() instanceof RadioSongDiscItem disc)) {
            return false;
        }
        List<String> songs = recordedSongs();
        return songs.size() < MAX_TRACKS && !songs.contains(disc.songId());
    }

    private boolean recordDisc() {
        ItemStack input = inputDisc();
        if (!(input.getItem() instanceof RadioSongDiscItem disc) || !canRecord()) {
            return false;
        }

        ItemStack radio = radioStack();
        if (!(radio.getItem() instanceof RadioItem)) {
            return false;
        }
        CompoundTag tag = radio.getOrCreateTag();
        ListTag list = tag.getList(TAG_TRACKS, Tag.TAG_STRING);
        list.add(StringTag.valueOf(disc.songId()));
        tag.put(TAG_TRACKS, list);

        input.shrink(1);
        if (input.isEmpty()) {
            discInput.setItem(0, ItemStack.EMPTY);
        }

        discInput.setChanged();
        player.getInventory().setChanged();
        broadcastChanges();
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        return id == BUTTON_RECORD && recordDisc();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();

        if (index == 0) {
            if (!moveItemStackTo(source, 1, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (source.getItem() instanceof RadioSongDiscItem) {
            if (!moveItemStackTo(source, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, source);
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            ItemStack remaining = discInput.removeItemNoUpdate(0);
            if (!remaining.isEmpty()) {
                player.getInventory().placeItemBackInInventory(remaining);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(hand).getItem() instanceof RadioItem;
    }
}
