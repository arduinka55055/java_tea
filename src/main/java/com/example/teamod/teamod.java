package com.example.teamod;

import com.example.teamod.registers.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.example.proxy.CommonProxy;

@Mod(modid = teamod.MODID, name = teamod.NAME, version = teamod.VERSION)
public class teamod {
    public static final String MODID = "tea";
    public static final String NAME = "TEA MOD";
    public static final String VERSION = "1.0";

    public static class BlockCropTea extends BlockCrops {

        private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {

                new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.25D, 0.5625D),
                new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D),
                new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D),
                new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5625D, 0.625D),
                new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.6875D, 0.625D),
                new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D),
                new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D),
                new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D) };

        public BlockCropTea(final String cropName) {

            this.setUnlocalizedName(cropName);
            this.setRegistryName(cropName);
        }

        public AxisAlignedBB getBoundingBox(final IBlockState blockState, final IBlockAccess source,
                final BlockPos position) {

            return CROPS_AABB[(blockState.getValue(this.getAgeProperty())).intValue()];
        }

        @Override
        public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos,
                final IBlockState state, final int fortune) {
            super.getDrops(drops, world, pos, state, fortune);
            if (this.getAge(state) == 7) {
                drops.add(new ItemStack(ItemsRegistry.TEA, (int) Math.floor(Math.random() * 9)));
                drops.add(new ItemStack(ItemsRegistry.TEA_SEED, (int) Math.floor(Math.random() * 3)));
            }
        }

        @Override
        protected Item getSeed() {

            return ItemsRegistry.TEA_SEED;
        }

        @Override
        protected Item getCrop() {
            return ItemsRegistry.TEA;
        }

        protected int getBonemealAgeIncrease(final World world) {

            return MathHelper.getInt(world.rand, 1, 5);
        }

        public EnumPlantType getPlantType(final IBlockAccess world, final BlockPos position) {

            return EnumPlantType.Crop;
        }
    }

    public static class ItemTeaSeeds extends ItemSeeds {
        public ItemTeaSeeds(final String name) {
            super(BlocksRegistry.CROP_TEA, Blocks.FARMLAND);
            setUnlocalizedName(name);
            setRegistryName(name);
        }
    }

    public static class ItemTea extends Item {
        public ItemTea(final String name) {
            this.setUnlocalizedName(name);
            this.setRegistryName(name);
        }
    }

    public static class ItemColdTeaBucket extends FoodBucket {
        public ItemColdTeaBucket(final String name, final int hungerPointsRestoration,
                final float saturationPointsRestoration, final boolean isSuitableForWolves) {
            super(hungerPointsRestoration, saturationPointsRestoration, isSuitableForWolves,
                    BlocksRegistry.cold_teafluidBlock);
            this.setContainerItem(Items.BUCKET);
            this.setMaxStackSize(1);
            this.setUnlocalizedName(name);
            this.setRegistryName(name);
        }

        @Override
        protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
            super.onFoodEaten(stack, worldIn, player);
            player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
        }
    }

    public static class ItemTeaBucket extends FoodBucket {
        public ItemTeaBucket(final String name, final int hungerPointsRestoration,
                final float saturationPointsRestoration, final boolean isSuitableForWolves) {
            super(hungerPointsRestoration, saturationPointsRestoration, isSuitableForWolves,
                    BlocksRegistry.teafluidBlock);

            this.setMaxStackSize(1);
            this.setUnlocalizedName(name);
            this.setRegistryName(name);
        }

        @Override
        protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
            super.onFoodEaten(stack, worldIn, player);
            player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
        }
    }

    public static class CustomFluid extends Fluid {
        /**
         * @param fluidName - Название жидкости
         * @param still     - Название файла "стоячий" жидкости
         * @param flowing   - Название файла "текучей" жидкости
         */
        public CustomFluid(String fluidName, String still, String flowing) {
            super(fluidName, new ResourceLocation("tea", "fluid/" + still),
                    new ResourceLocation("tea", "fluid/" + flowing));
        }
    }

    public static class BlockFluid extends BlockFluidClassic {

        public BlockFluid(Fluid fluid, String name) {
            super(fluid, new MaterialLiquid(MapColor.WATER));
            setUnlocalizedName(name);
            setRegistryName(name);
        }
    }

    // INIT SEGMENT
    @SidedProxy(clientSide = "com.example.proxy.ClientProxy", serverSide = "com.example.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
