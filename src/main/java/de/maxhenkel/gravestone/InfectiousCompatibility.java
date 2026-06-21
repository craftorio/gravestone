package de.maxhenkel.gravestone;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public final class InfectiousCompatibility {

    private static final ResourceLocation INFECTION_EFFECT_ID = new ResourceLocation("infectious", "infection");

    private static MobEffect infectionEffect;

    private InfectiousCompatibility() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded("infectious");
    }

    public static int getInfectionLevel(LivingEntity entity) {
        MobEffect effect = getInfectionEffect();
        if (effect == null) {
            return -1;
        }
        MobEffectInstance instance = entity.getEffect(effect);
        if (instance == null) {
            return -1;
        }
        return instance.getAmplifier();
    }

    public static boolean shouldSkipGrave(LivingEntity entity) {
        if (!isLoaded() || !Main.SERVER_CONFIG.infectiousCompatibility.get()) {
            return false;
        }
        int level = getInfectionLevel(entity);
        return level >= 0 && level > Main.SERVER_CONFIG.infectiousLevelThreshold.get();
    }

    private static MobEffect getInfectionEffect() {
        if (!isLoaded()) {
            return null;
        }
        if (infectionEffect == null) {
            infectionEffect = ForgeRegistries.MOB_EFFECTS.getValue(INFECTION_EFFECT_ID);
        }
        return infectionEffect;
    }
}
