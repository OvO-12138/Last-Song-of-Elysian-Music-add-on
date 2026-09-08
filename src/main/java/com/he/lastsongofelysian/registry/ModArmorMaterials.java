package com.he.lastsongofelysian.registry;

import com.he.lastsongofelysian.lastsongofelysian;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {

    NAMELESS_TOWN(
            lastsongofelysian.MODID + ":nameless_town",
            25,
            10,
            15,
            SoundEvents.ARMOR_EQUIP_GOLD,
            1.0F,
            0.0F,
            () -> Ingredient.EMPTY
    );

    private final String name;
    private final int durabilityMultiplier;
    private final int chestplateDefense;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmorMaterials(String name,
                      int durabilityMultiplier,
                      int chestplateDefense,
                      int enchantmentValue,
                      SoundEvent equipSound,
                      float toughness,
                      float knockbackResistance,
                      Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.chestplateDefense = chestplateDefense;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 11 * durabilityMultiplier;
            case CHESTPLATE -> 16 * durabilityMultiplier;
            case LEGGINGS -> 15 * durabilityMultiplier;
            case BOOTS -> 13 * durabilityMultiplier;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return type == ArmorItem.Type.CHESTPLATE ? chestplateDefense : 0;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
