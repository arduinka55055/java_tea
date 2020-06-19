package com.example.teamod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Int;

import java.util.HashMap;
import java.util.Map;

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

    public static class ItemColdTeaBucket extends ItemFood {
        public ItemColdTeaBucket(final String name, final int hungerPointsRestoration,
                final float saturationPointsRestoration, final boolean isSuitableForWolves) {
            super(hungerPointsRestoration, saturationPointsRestoration, isSuitableForWolves);
            this.setMaxStackSize(1);
            this.setUnlocalizedName(name);
            this.setRegistryName(name);
        }

        @Override
        protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
            super.onFoodEaten(stack, worldIn, player);
            // player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));
        }
    }

    public static class ItemTeaBucket extends FoodBucket {
        public ItemTeaBucket(final String name, final int hungerPointsRestoration,
                final float saturationPointsRestoration, final boolean isSuitableForWolves) {
            super(hungerPointsRestoration, saturationPointsRestoration, isSuitableForWolves,BlocksRegistry.OIL);
            
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

    public static class BlockFluid extends BlockFluidClassic {
        /**
         * Это конструктор который передаст наши значения родителю.
         * 
         * @param fluid - это наша жидкость
         * @param name  - это материал нашего блока. Существует два метериала, Water -
         *              ведёт себя как вода, быстро течёт Lava - ведёт себя как лава,
         *              медленно течёт.
         */
        public BlockFluid(Fluid fluid, String name) {
            super(fluid, Material.WATER);
            setUnlocalizedName(name);
            setRegistryName(name);
        }
        @Override
        public void onBlockAdded(World world, BlockPos pos, IBlockState state)
        {
            super.onBlockAdded(world, pos, state);
            mergerFluids(pos, world);
        }      
        /**
         * Это проверка на соседние блоки.
         */
        //@Override
        public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock)
        {
            super.neighborChanged(state, world, pos,neighborBlock ,pos);
            mergerFluids(pos, world);
        }
     
        private void mergerFluids(BlockPos pos, World world)
        {
            for(EnumFacing facing : EnumFacing.values())
            {
                Block block = world.getBlockState(pos.offset(facing)).getBlock();
                //Если вода, то ставим камень
                if(block == Blocks.WATER || block == Blocks.FLOWING_WATER)
                {
                    world.setBlockState(pos.offset(facing), Blocks.STONE.getDefaultState());
                }
                //Если лава, ставим кирпичный блок
                else if(block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
                {
                    world.setBlockState(pos.offset(facing), Blocks.BRICK_BLOCK.getDefaultState());
                }
            }
        }

    }



    public static class CustomFluid extends Fluid {
        /**
         * Конструктор нашей жидкости
         * 
         * @param fluidName - Название жидкости
         * @param still     - Название файла "стоячий" жидкости
         * @param flowing   - Название файла "текучей" жидкости
         */
        public CustomFluid(String fluidName, String still, String flowing) {
            super(fluidName, new ResourceLocation("tea", still),
                    new ResourceLocation("tea", flowing));      
        }
        /**
         * Так же мы можем задать цвет жидкости. Последние шесть цифр после 0xFF, это
         * наш цвет. Если цвет не нужен, то просто удалите этот метод.
         
        @Override
        public int getColor() {
            return 0xFFFFFF00;
        }
        */
    }


    public static class BucketHandler {

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
            System.out.println("////////////////////////////////////////////////////");
            System.out.println(raytraceresult);
            System.out.println("////////////////////////////////////////////////////");
            BlockPos pos = raytraceresult.getBlockPos();
            Block targetblock = world.getBlockState(pos).getBlock();
            Item bucket = buckets.get(targetblock);
            if (bucket != null) {
                world.setBlockToAir(pos);
                System.out.println(targetblock);
                myEvent.setFilledBucket(new ItemStack(ItemsRegistry.TEA_BUCKET,1));
                myEvent.setResult(Result.ALLOW);
                return new ItemStack(bucket);
            } else
                return null;

        }
    }

    /*
     * ____ _ _ | _ \ ___ __ _(_)___| |_ _ __ _ _ | |_) / _ \/ _` | / __| __| '__| |
     * | | | _ < __/ (_| | \__ \ |_| | | |_| | |_| \_\___|\__, |_|___/\__|_| \__, |
     * |___/ |___/
     */

    public static class FluidsRegister {
        /**
         * Переменные с нашими жидкостями.
         */
        public static Fluid OIL = new CustomFluid("oil", "oil_still", "oil_flow");

        /**
         * Регистрация нашей жидкости
         */
        public static void register() {
            FluidRegistry.registerFluid(OIL);
            //FluidRegistry.addBucketForFluid(OIL);
        }

        /**
         * Регистрация модели нашей жидкости
         */
        public static void registerRender() {
            /**
             * Я упростил регистрацию, и теперь, чтобы зарегистрировать жидкость нужно
             * прописать метод modelLoader с такими значениями: 1 - Блок 2 - Вариант
             * жидкости, в основном это fluid или gas, но про газ познее.
             */
            modelLoader(BlocksRegistry.OIL, "oil");
        }

        @SideOnly(Side.CLIENT)
        public static void modelLoader(Block block, String variant) {
            ModelResourceLocation milkLocation = new ModelResourceLocation("tea:fluid", variant);
            Item milk = Item.getItemFromBlock(block);

            ModelBakery.registerItemVariants(milk);
            ModelLoader.setCustomMeshDefinition(milk, stack -> milkLocation);
            ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return milkLocation;
                }
            });
        }
    }

    public static class BlocksRegistry {

        public static final Block CROP_TEA = new BlockCropTea("crop_tea");
        public static final Block OIL = new BlockFluid(FluidsRegister.OIL, "fluid_oil");

        public static void register() {
            setBlockRegister(OIL);
            setBlockRegister(CROP_TEA);
        }

        private static void setBlockRegister(final Block block) {

            ForgeRegistries.BLOCKS.register(block);
            ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    public static class ItemsRegistry {

        public static final Item TEA_SEED = new ItemTeaSeeds("tea_seed").setCreativeTab(CreativeTabs.FOOD),
                TEA = new ItemTea("tea").setCreativeTab(CreativeTabs.FOOD),
                COLD_TEA_BUCKET = new ItemColdTeaBucket("cold_tea_bucket", 2, 0.5F, false)
                        .setCreativeTab(CreativeTabs.FOOD),
                TEA_BUCKET = new ItemTeaBucket("tea_bucket", 7, 5F, false).setCreativeTab(CreativeTabs.FOOD);

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
