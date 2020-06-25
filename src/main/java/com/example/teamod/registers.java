package com.example.teamod;

import com.example.teamod.teamod.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class registers {
    public static class FluidsRegister {
        public static Fluid teafluid = new CustomFluid("teafluid", "teafluid_still", "teafluid_flow");
        public static Fluid cold_teafluid = new CustomFluid("coldteafluid", "coldteafluid_still", "coldteafluid_flow");
        public static void register() {
            FluidRegistry.registerFluid(teafluid);
            FluidRegistry.registerFluid(cold_teafluid);
        }

        public static void registerRender() {
            modelLoader(BlocksRegistry.teafluidBlock, "teafluid");
            modelLoader(BlocksRegistry.cold_teafluidBlock, "coldteafluid");
        }

        @SideOnly(Side.CLIENT)
        public static void modelLoader(Block block, String variant) {
            ModelResourceLocation myFluidLocation = new ModelResourceLocation("tea:fluid", variant);
            Item myfluid = Item.getItemFromBlock(block);

            ModelBakery.registerItemVariants(myfluid);
            ModelLoader.setCustomMeshDefinition(myfluid, stack -> myFluidLocation);
            ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return myFluidLocation;
                }
            });
        }
    }

    public static class BlocksRegistry {

        public static final Block CROP_TEA = new BlockCropTea("crop_tea");
        public static final Block teafluidBlock = new BlockFluid(FluidsRegister.teafluid, "teafluid");
        public static final Block cold_teafluidBlock = new BlockFluid(FluidsRegister.cold_teafluid, "coldteafluid");

        public static void register() {
            setBlockRegister(teafluidBlock);
            setBlockRegister(cold_teafluidBlock);
            setBlockRegister(CROP_TEA);
        }

        private static void setBlockRegister(final Block block) {

            ForgeRegistries.BLOCKS.register(block);
            ForgeRegistries.ITEMS.register(new
                    ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    public static class ItemsRegistry {

        public static final Item TEA_SEED = new ItemTeaSeeds("tea_seed").setCreativeTab(CreativeTabs.FOOD),
                TEA = new ItemTea("tea").setCreativeTab(CreativeTabs.FOOD),

                COLD_TEA_BUCKET = new ItemColdTeaBucket("cold_tea_bucket", 2, 0.5F, false)
                        .setCreativeTab(CreativeTabs.FOOD),

                TEA_BUCKET = new ItemTeaBucket("tea_bucket", 7, 5F, false)
                        .setCreativeTab(CreativeTabs.FOOD);

        public static void register() {
            setItemRegister(TEA_SEED);
            setItemRegister(TEA);
            setItemRegister(COLD_TEA_BUCKET);
            setItemRegister(TEA_BUCKET);
        }

        public static void registerRender() {
            setItemRender(TEA_SEED);
            setItemRender(TEA);
            setItemRender(COLD_TEA_BUCKET);
            setItemRender(TEA_BUCKET);
        }

        private static void setItemRegister(final Item item) {
            ForgeRegistries.ITEMS.register(item);
        }

        @SideOnly(Side.CLIENT)
        private static void setItemRender(final Item item) {

            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                    new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

    public static class CraftingRegister {
        public static void registerRecipes(final String name) {
            CraftingHelper.register(new ResourceLocation("tea", name),
                    (IRecipeFactory) (context, json) -> CraftingHelper.getRecipe(json, context));
        }
    }
}