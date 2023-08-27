package net.creep3rcrafter.mysticpotions.register;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.creep3rcrafter.mysticpotions.MysticPotions;
import net.creep3rcrafter.mysticpotions.utils.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MysticPotions.MOD_ID, Registries.MOB_EFFECT);

    /* IDEAS
    Inversion - Swaps good/bad potions with eachother
    Luminous - things glow
    Menacing - mobs are scared of you
    Iron Fist - break blocks with your fist with an iron pick effeciency
    Light Footed - Doesnt set off pressure plates or trip wire
    Energized - messes with redstone
    Water Miner - you can mine faster underwater
    Calming - stops angered mobs from being angry
    Xp boost - xp multipliyer
    Smelting - smelts items in your inventory
    Refinement/Potency - Makes potion effects last longer or stronger
    Herding - makes all animals follow you
    Knock-back resistance - self explanitory
    Stealth - you dont make noises
    Nureshment potion
    Invincibility/overkilled- but delayed damage and make stackable - maybe make antidote
    Holy Water - hurts hostile mobs and if they hurt you they also take the same ammount of damage
    fast falling
    Extension/Extension Cooldown
    max health potion

    Done------------------------------------
    Undying - totem of undieing just without the item
    Homing - teleports you to your bed
    Recovery - sends you to your last death location
    Teleportation - randomly teleports you
    Nullifier - removes all potion effects and stops all potion effects from happening
    Air Swim - swim in the air
    */

    //Beneficial
    public static final RegistrySupplier<MobEffect> UNDYING = EFFECTS.register("undying", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16766527) {
        public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        }

        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        public boolean isInstantenous() {
            return false;
        }
    });//New

    /*
    public static final RegistrySupplier<MobEffect> IRON_FIST = EFFECTS.register("iron_fist", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16766527) {
        public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        }

        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        public boolean isInstantenous() {
            return false;
        }
    });//New
     */

    public static final RegistrySupplier<MobEffect> AIR_SWIM = EFFECTS.register("air_swim", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 24991) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            super.applyEffectTick(livingEntity, amplifier);
            if (livingEntity.isSprinting()) {
                if (!livingEntity.isPassenger()) {
                    if (!livingEntity.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                        if (!livingEntity.isEyeInFluid(FluidTags.WATER)) {
                            if (!livingEntity.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014D)) {
                                float f;
                                livingEntity.resetFallDistance();
                                livingEntity.wasEyeInWater = true;
                                livingEntity.wasTouchingWater = true;
                                livingEntity.setSwimming(true);
                                Vec3 vec32 = livingEntity.getDeltaMovement();
                                f = 1.08f;
                                if (livingEntity.horizontalCollision && livingEntity.onClimbable()) {
                                    vec32 = new Vec3(vec32.x, 0.25D, vec32.z);
                                }
                                livingEntity.setDeltaMovement(vec32.multiply(f, f + 0.05, f));
                                livingEntity.gameEvent(GameEvent.SWIM);
                            }
                        }
                    }
                }
            }
            //livingEntity.updateSwimming();
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });//New
    public static final RegistrySupplier<MobEffect> WARMING = EFFECTS.register("warming", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16757504) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) livingEntity.getLevel();
                livingEntity.setTicksFrozen(0);
                if (livingEntity instanceof SnowGolem && serverLevel.getServer().getTickCount() % 20 == 0) {
                    livingEntity.hurt(livingEntity.damageSources().magic(), amplifier + 1);
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });//New
    public static final RegistrySupplier<MobEffect> HOMING = EFFECTS.register("homing", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 16736892) {

        @Override
        public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                if (livingEntity instanceof ServerPlayer serverPlayer && !livingEntity.isSpectator()) {
                    Vec3 pos;
                    if (serverPlayer.getRespawnPosition() != null && (serverPlayer.getLevel().getBlockState(serverPlayer.getRespawnPosition()).getBlock() instanceof BedBlock)) {
                        pos = Vec3.atBottomCenterOf(serverPlayer.getRespawnPosition());
                        serverPlayer.connection.teleport(pos.x, pos.y, pos.z, Mth.wrapDegrees(serverPlayer.getYRot()), Mth.wrapDegrees(serverPlayer.getXRot()));
                    } else {
                        pos = Vec3.atBottomCenterOf(serverPlayer.getLevel().getSharedSpawnPos());
                        serverPlayer.connection.teleport(pos.x, pos.y, pos.z, Mth.wrapDegrees(serverPlayer.getYRot()), Mth.wrapDegrees(serverPlayer.getXRot()));
                    }
                } else {
                    Vec3 pos = Vec3.atBottomCenterOf(livingEntity.getLevel().getSharedSpawnPos());
                    livingEntity.teleportTo(pos.x, pos.y, pos.z);
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration == 1;
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    });//New
    public static final RegistrySupplier<MobEffect> RECOVERY = EFFECTS.register("recovery", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 9044042) {
        @Override
        public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                if (livingEntity instanceof ServerPlayer serverPlayer && !livingEntity.isSpectator()) {
                    if (serverPlayer.getLastDeathLocation().isPresent()) {
                        if (serverPlayer.getLevel().dimension() == serverPlayer.getLastDeathLocation().get().dimension()) {
                            Vec3 pos = Vec3.atBottomCenterOf(serverPlayer.getLastDeathLocation().get().pos());
                            serverPlayer.connection.teleport(pos.x, pos.y, pos.z, Mth.wrapDegrees(serverPlayer.getYRot()), Mth.wrapDegrees(serverPlayer.getXRot()));
                        }
                    }
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration == 1;
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    });//New
    //Neutral
    public static final RegistrySupplier<MobEffect> TELEPORTATION = EFFECTS.register("teleportation", () -> new MobEffect(MobEffectCategory.NEUTRAL, 13041919) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                if (livingEntity.getLevel().getServer() != null) {
                    if (livingEntity.getLevel().getServer().getTickCount() % (20 + livingEntity.getRandom().nextInt(-10, 40)) == 0) {
                        ServerLevel level = livingEntity.getLevel().getServer().getLevel(livingEntity.getLevel().dimension());
                        assert level != null;
                        if (!level.isClientSide) {
                            double d = livingEntity.getX();
                            double e = livingEntity.getY();
                            double f = livingEntity.getZ();
                            for (int i = 0; i < 32; ++i) {
                                double g = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
                                double h = Mth.clamp(livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(32) - 16), (double) level.getMinBuildHeight(), (double) (level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1));
                                double j = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 32.0D;
                                if (livingEntity.isPassenger()) {
                                    livingEntity.stopRiding();
                                }

                                Vec3 vec3 = livingEntity.position();
                                if (livingEntity.randomTeleport(g, h, j, true)) {
                                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(livingEntity));
                                    SoundEvent soundEvent = livingEntity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                                    level.playSound((Player) null, d, e, f, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    livingEntity.playSound(soundEvent, 1.0F, 1.0F);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });//New
    public static final RegistrySupplier<MobEffect> SPLIPPERY = EFFECTS.register("slippery", () -> new MobEffect(MobEffectCategory.NEUTRAL, 1572863) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration > 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
    //Harmful
    public static final RegistrySupplier<MobEffect> THUNDEROUS = EFFECTS.register("thunderous", () -> new MobEffect(MobEffectCategory.HARMFUL, 14745599) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) livingEntity.getLevel();
                Utils.lightning(livingEntity, serverLevel, amplifier);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration == 1;
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    });
    public static final RegistrySupplier<MobEffect> EXPLOSIVE = EFFECTS.register("explosive", () -> new MobEffect(MobEffectCategory.HARMFUL, 4522008) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) livingEntity.getLevel();
                if (!livingEntity.isSpectator()) {
                    if (livingEntity.getLevel().dimension() == Level.NETHER) {
                        Utils.explode(serverLevel, livingEntity.blockPosition(), amplifier, true);
                    } else {
                        if (amplifier > 4) {
                            Utils.explode(serverLevel, livingEntity.blockPosition(), amplifier, true);
                        } else {
                            Utils.explode(serverLevel, livingEntity.blockPosition(), amplifier);
                        }
                    }
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration == 1;
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    });
    public static final RegistrySupplier<MobEffect> BURNING = EFFECTS.register("burning", () -> new MobEffect(MobEffectCategory.HARMFUL, 16740608) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.isInWaterRainOrBubble()) {
                livingEntity.setSecondsOnFire(1);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
    public static final RegistrySupplier<MobEffect> FREEZING = EFFECTS.register("freezing", () -> new MobEffect(MobEffectCategory.HARMFUL, 1572863) {
        @Override
        public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
            if (livingEntity.canFreeze()) {
                livingEntity.setIsInPowderSnow(true);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
    public static final RegistrySupplier<MobEffect> CORROSIVE = EFFECTS.register("corrosive", () -> new MobEffect(MobEffectCategory.HARMFUL, 10157824) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (!livingEntity.getLevel().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) livingEntity.getLevel();
                Random random = new Random();
                if (!livingEntity.isSpectator() && serverLevel.getServer().getTickCount() % 10 == 0) {
                    MinecraftServer server = serverLevel.getServer();
                    if (livingEntity instanceof ServerPlayer) {
                        List<Item> items = new ArrayList<Item>();
                        items.add(Items.IRON_NUGGET);
                        items.add(Items.IRON_ORE);
                        items.add(Items.IRON_INGOT);
                        items.add(Items.COPPER_INGOT);
                        items.add(Items.NETHERITE_SCRAP);
                        items.add(Items.NETHERITE_INGOT);

                        List<Item> results = Utils.recipesContainsItems(server, RecipeType.CRAFTING, items);
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.SMELTING, items));
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.SMITHING, items));
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.BLASTING, items));
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.SMOKING, items));
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.STONECUTTING, items));
                        results.addAll(Utils.recipesContainsItems(server, RecipeType.CAMPFIRE_COOKING, items));

                        List<Item> resultsWithoutDuplicates = new ArrayList<Item>(new HashSet<>(results));

                        Inventory inventory = ((ServerPlayer) livingEntity).getInventory();
                        for (int i = 0; i < inventory.getContainerSize(); i++) {
                            Item item = inventory.getItem(i).getItem();
                            ItemStack itemStack = inventory.getItem(i);
                            if (itemStack.isDamageableItem()) {
                                Utils.damageItem(livingEntity, itemStack, 1 + amplifier);
                            } else if (!itemStack.isDamageableItem() && resultsWithoutDuplicates.contains(item)) {
                                if (random.nextInt(100) >= 90) {
                                    if (random.nextInt(3) <= amplifier) {
                                        itemStack.shrink(1);
                                        //livingEntity.playSound(Registry.SOUND_EVENT.get(new ResourceLocation("entity.item.break")));
                                    }
                                }
                            }
                        }
                    } else if (livingEntity instanceof IronGolem) {
                        livingEntity.hurt(livingEntity.damageSources().magic(), 1f + amplifier);//take iron damage
                    } else {
                        if (livingEntity.hasItemInSlot(EquipmentSlot.MAINHAND)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.MAINHAND, 1 + amplifier);
                        }
                        if (livingEntity.hasItemInSlot(EquipmentSlot.OFFHAND)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.OFFHAND, 1 + amplifier);
                        }
                        if (livingEntity.hasItemInSlot(EquipmentSlot.HEAD)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.HEAD, 1 + amplifier);
                        }
                        if (livingEntity.hasItemInSlot(EquipmentSlot.CHEST)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.CHEST, 1 + amplifier);
                        }
                        if (livingEntity.hasItemInSlot(EquipmentSlot.LEGS)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.LEGS, 1 + amplifier);
                        }
                        if (livingEntity.hasItemInSlot(EquipmentSlot.FEET)) {
                            Utils.damageItem(livingEntity, EquipmentSlot.FEET, 1 + amplifier);
                        }
                    }
                }
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
    public static final RegistrySupplier<MobEffect> GRAVITATION = EFFECTS.register("gravitation", () -> new MobEffect(MobEffectCategory.HARMFUL, 11350783) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
    /*
    public static final RegistrySupplier<MobEffect> HOLY_WATER = EFFECTS.register("holy_water", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 12989085) {
        @Override
        public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
            if (livingEntity instanceof Zombie){
                livingEntity.hurt(DamageSource.MAGIC, 5f);
            }
            if (livingEntity instanceof Player){
                livingEntity.heal(5f);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            return duration >= 1;
        }

        @Override
        public boolean isInstantenous() {
            return false;
        }
    });
     */
}
