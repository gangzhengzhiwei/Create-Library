package com.petrolpark.data.reward;

import java.util.stream.Stream;
import java.util.function.UnaryOperator;

import com.petrolpark.data.IEntityTarget;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class AbstractGiveEntityItemsReward extends ContextEntityReward {

    public AbstractGiveEntityItemsReward(IEntityTarget target) {
        super(target);
    };

    public abstract Stream<ItemStack> streamStacks(Entity recipient, LootContext context);

    @Override
    public final void reward(Entity entity, LootContext context, float multiplier) {
        if (entity instanceof InventoryCarrier hasInv) {
            streamStacks(entity, context).map(multiplyAmount(multiplier)).forEach(stack -> entity.spawnAtLocation(ItemHandlerHelper.insertItemStacked(new InvWrapper(hasInv.getInventory()), stack, false)));
        } else if (entity instanceof Player player) {
            streamStacks(entity, context).map(multiplyAmount(multiplier)).forEach(player.getInventory()::placeItemBackInInventory);
        } else {
            streamStacks(entity, context).map(multiplyAmount(multiplier)).forEach(entity::spawnAtLocation);
        };
    };

    private UnaryOperator<ItemStack> multiplyAmount(float multiplier) {
        return stack -> stack.copyWithCount(Math.min(stack.getMaxStackSize(), (int)(multiplier * (float)stack.getCount())));
    };
    
};
