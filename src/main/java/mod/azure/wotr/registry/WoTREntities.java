package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.entity.LungSerpentEntity;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class WoTREntities {

	public static final EntityType<DrakeEntity> DRAKE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation(WoTRMod.MODID, "drake"),
			FabricEntityTypeBuilder.create(MobCategory.MONSTER, DrakeEntity::new)
					.dimensions(EntityDimensions.fixed(2.3f, 2.15F)).trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static final EntityType<LungSerpentEntity> LUNG_SERPENT = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation(WoTRMod.MODID, "lung_serpent"),
			FabricEntityTypeBuilder.create(MobCategory.MONSTER, LungSerpentEntity::new)
					.dimensions(EntityDimensions.fixed(1.3f, 1.15F)).trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static EntityType<DrakeGauntletFireProjectile> DRAKE_GAUNTLET_FIRE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(WoTRMod.MODID, "drake_gauntlet_fire"),
			FabricEntityTypeBuilder
					.<DrakeGauntletFireProjectile>create(MobCategory.MISC, DrakeGauntletFireProjectile::new)
					.dimensions(new EntityDimensions(1.5F, 1.5F, false)).disableSummon().spawnableFarFromPlayer()
					.fireImmune().trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static EntityType<DrakeFireProjectile> DRAKE_FIRE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation(WoTRMod.MODID, "drake_fire"),
			FabricEntityTypeBuilder.<DrakeFireProjectile>create(MobCategory.MISC, DrakeFireProjectile::new)
					.dimensions(new EntityDimensions(1.5F, 1.5F, false)).disableSummon().spawnableFarFromPlayer()
					.fireImmune().trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static BlockEntityType<DrakeSkullEntity> DRAKE_SKULL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
			WoTRMod.MODID + ":drake_skull",
			FabricBlockEntityTypeBuilder.create(DrakeSkullEntity::new, WoTRBlocks.DRAKE_SKULL).build(null));

}
