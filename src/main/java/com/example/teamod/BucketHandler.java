package com.example.teamod;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public final class BucketHandler {

    public static BucketHandler INSTANCE = new BucketHandler();
    public Map<Block, Item> buckets = new HashMap<Block, Item>();

    private BucketHandler() {
    }

    @SubscribeEvent
    public ItemStack onBucketFilled(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget(),event);
        return result;
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult raytraceresult,FillBucketEvent myEvent) {
        BlockPos pos = raytraceresult.getBlockPos();
        Block targetblock = world.getBlockState(pos).getBlock();
        Item bucket = buckets.get(targetblock);
        if (bucket != null) {
            world.setBlockToAir(pos);
            System.out.println(targetblock);
            myEvent.setFilledBucket(new ItemStack(bucket,1));
            myEvent.setResult(Result.ALLOW);
            return new ItemStack(bucket);
        } else
            return null;

    }
}