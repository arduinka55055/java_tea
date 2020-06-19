package com.example.proxy;

import com.example.teamod.registers.*;
import com.example.teamod.BucketHandler;
import com.example.teamod.teamod;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
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
        BucketHandler.INSTANCE.buckets.put(BlocksRegistry.teafluidBlock, ItemsRegistry.TEA_BUCKET);
        BucketHandler.INSTANCE.buckets.put(BlocksRegistry.cold_teafluidBlock, ItemsRegistry.COLD_TEA_BUCKET);
    }

    public void init(FMLInitializationEvent event)
    {
        CraftingRegister.registerRecipes("cold_tea_bucket");

        /*Ingredient[] ingredientlist= new Ingredient[2];
        Item fuckwaterbucket = Items.LAVA_BUCKET;
        fuckwaterbucket.setContainerItem(null);
        ingredientlist[0] = Ingredient.fromItem(fuckwaterbucket);
        ingredientlist[1] = Ingredient.fromItem(ItemsRegistry.TEA);
        GameRegistry.addShapelessRecipe(new ResourceLocation(teamod.MODID),null, new ItemStack(ItemsRegistry.COLD_TEA_BUCKET,1),ingredientlist);
        */
        
        GameRegistry.addSmelting(ItemsRegistry.COLD_TEA_BUCKET, new ItemStack(ItemsRegistry.TEA_BUCKET), (float) 1.0);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}