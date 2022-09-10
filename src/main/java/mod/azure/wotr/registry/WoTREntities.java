package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.blocks.tile.TickingLightEntity;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WoTREntities {

	public static final EntityType<DrakeEntity> DRAKE = Registry.register(Registry.ENTITY_TYPE,
			new Identifier(WoTRMod.MODID, "drake"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DrakeEntity::new)
					.dimensions(EntityDimensions.fixed(2.3f, 2.15F)).trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static EntityType<DrakeGauntletFireProjectile> DRAKE_GAUNTLET_FIRE = Registry.register(Registry.ENTITY_TYPE,
			new Identifier(WoTRMod.MODID, "drake_gauntlet_fire"),
			FabricEntityTypeBuilder
					.<DrakeGauntletFireProjectile>create(SpawnGroup.MISC, DrakeGauntletFireProjectile::new)
					.dimensions(new EntityDimensions(1.5F, 1.5F, false)).disableSummon().spawnableFarFromPlayer()
					.fireImmune().trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static EntityType<DrakeFireProjectile> DRAKE_FIRE = Registry.register(Registry.ENTITY_TYPE,
			new Identifier(WoTRMod.MODID, "drake_fire"),
			FabricEntityTypeBuilder.<DrakeFireProjectile>create(SpawnGroup.MISC, DrakeFireProjectile::new)
					.dimensions(new EntityDimensions(1.5F, 1.5F, false)).disableSummon().spawnableFarFromPlayer()
					.fireImmune().trackRangeBlocks(90).trackedUpdateRate(1).build());

	public static final BlockEntityType<TickingLightEntity> TICKING_LIGHT_ENTITY = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, WoTRMod.MODID + ":lightblock",
			FabricBlockEntityTypeBuilder.create(TickingLightEntity::new, WoTRBlocks.TICKING_LIGHT_BLOCK).build(null));

	public static BlockEntityType<DrakeSkullEntity> DRAKE_SKULL = Registry.register(Registry.BLOCK_ENTITY_TYPE,
			WoTRMod.MODID + ":drake_skull",
			FabricBlockEntityTypeBuilder.create(DrakeSkullEntity::new, WoTRBlocks.TICKING_LIGHT_BLOCK).build(null));

}
