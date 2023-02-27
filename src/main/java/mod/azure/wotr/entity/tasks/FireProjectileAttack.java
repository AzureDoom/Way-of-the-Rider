package mod.azure.wotr.entity.tasks;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.wotr.entity.WoTREntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.util.BrainUtils;

public class FireProjectileAttack<E extends WoTREntity> extends CustomRangedDelayedBehaviour<E> {
	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
			Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
			Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

	protected Function<E, Integer> attackIntervalSupplier = entity -> 20;

	@Nullable
	protected LivingEntity target = null;

	public FireProjectileAttack(int delayTicks) {
		super(delayTicks);
	}

	/**
	 * Set the time between attacks.
	 * 
	 * @param supplier The tick value provider
	 * @return this
	 */
	public FireProjectileAttack<E> attackInterval(Function<E, Integer> supplier) {
		this.attackIntervalSupplier = supplier;

		return this;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		this.target = BrainUtils.getTargetOfEntity(entity);

		return entity.getSensing().hasLineOfSight(this.target);
	}

	@Override
	protected void start(E entity) {
		entity.swing(InteractionHand.MAIN_HAND);
		BehaviorUtils.lookAtEntity(entity, this.target);
		entity.setAttackingState(0);
	}

	@Override
	protected void stop(E entity) {
		this.target = null;
		entity.setAttackingState(0);
	}

	@Override
	protected void doDelayedAction(E entity) {
		BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true,
				this.attackIntervalSupplier.apply(entity));

		if (this.target == null)
			return;
		
		if (entity.isWithinMeleeAttackRange(this.target))
			return;

		if (!entity.getSensing().hasLineOfSight(this.target))
			return;

		int j;
		for (j = 0; j < 15; ++j) {
			entity.shootFlames(this.target);
		}
	}

}
