package com.example.proxy;

import com.example.teamod.teamod.BlocksRegistry;
import com.example.teamod.teamod.BucketHandler;
import com.example.teamod.teamod.CraftingRegister;
import com.example.teamod.teamod.FluidsRegister;
import com.example.teamod.teamod.ItemsRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        FluidsRegister.register();
    	BlocksRegistry.register();
        ItemsRegistry.register();
        MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
        BucketHandler.INSTANCE.buckets.put(BlocksRegistry.OIL, ItemsRegistry.TEA_BUCKET);
    }

    public void init(FMLInitializationEvent event)
    {
    	CraftingRegister.registerRecipes("cold_tea_bucket");
    	GameRegistry.addSmelting(ItemsRegistry.COLD_TEA_BUCKET, new ItemStack(ItemsRegistry.TEA_BUCKET), (float) 1.0);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}