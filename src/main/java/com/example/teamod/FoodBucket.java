package com.example.teamod;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class FoodBucket extends ItemFood {/* Just copy-paste official code with bucket, like Pineapple apple pen */
    private final Block containedBlock;

    public FoodBucket(int amount, float saturation, boolean isWolfFood, Block containedBlockIn) {
        super(amount, saturation, isWolfFood);
        this.containedBlock = containedBlockIn;
        this.maxStackSize = 1;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        boolean flag = this.containedBlock == Blocks.AIR;
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);

        if (playerIn.canEat(false) && !playerIn.isSneaking()) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else if(raytraceresult != null){

            if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (worldIn.isBlockModifiable(playerIn, blockpos)) {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos
                        : blockpos.offset(raytraceresult.sideHit);

                if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)
                        && playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemstack)) {

                    if (playerIn instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, blockpos1, itemstack);
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode
                            ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET))
                            : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
            }
        }
               
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }



    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn) {
        if (this.containedBlock != Blocks.AIR) {
            IBlockState iblockstate = worldIn.getBlockState(posIn);
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

            if (worldIn.isAirBlock(posIn) || (flag && flag1)) {
                if (worldIn.provider.doesWaterVaporize()) {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
                            2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int k = 0; k < 8; ++k) {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) l + Math.random(),
                                (double) i + Math.random(), (double) j + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else {
                    if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
            @Nullable net.minecraft.nbt.NBTTagCompound nbt) {
        if (this.getClass() == FoodBucket.class) {
            return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
        } else {
            return super.initCapabilities(stack, nbt);
        }
    }
}