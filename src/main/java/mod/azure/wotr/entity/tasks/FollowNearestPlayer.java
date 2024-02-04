package mod.azure.wotr.entity.tasks;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.wotr.entity.WoTREntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class FollowNearestPlayer<E extends WoTREntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT));

    protected BiFunction<E, Player, Float> speedMod = (entity, potentialPlayer) -> 1f;
    protected BiFunction<E, Player, Float> closeEnoughWhen = (entity, potentialPlayer) -> 4f;
    protected BiConsumer<E, Player> closeEnoughAction = (entity, potentialPlayer) -> {};

    public FollowNearestPlayer<E> speedMod(BiFunction<E, Player, Float> function) {
        this.speedMod = function;

        return this;
    }

    public FollowNearestPlayer<E> closeEnoughDist(BiFunction<E, Player, Float> function) {
        this.closeEnoughWhen = function;

        return this;
    }

    public FollowNearestPlayer<E> closeEnoughAction(BiConsumer<E, Player> consumer) {
        this.closeEnoughAction = consumer;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return startCondition.test(entity);
    }

    @Override
    protected void tick(E entity) {
        Player nearestPlayer = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_PLAYER);

        if (nearestPlayer != null) {
            float closeEnough = this.closeEnoughWhen.apply(entity, nearestPlayer);

            if (entity.distanceToSqr(nearestPlayer) < closeEnough * closeEnough) { // close
                BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
                closeEnoughAction.accept(entity, nearestPlayer);
            }
            else { // not close
                BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(nearestPlayer, this.speedMod.apply(entity, nearestPlayer), (int) closeEnough));
            }
        }
    }
}
