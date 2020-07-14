package com.teamacronymcoders.base.subblocksystem.blocks;

import com.teamacronymcoders.base.client.models.generator.generatedmodel.IGeneratedModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ISubBlock {
    String getName();

    String getUnLocalizedName();

    default String getLocalizedName() {
        //noinspection deprecation
        return I18n.translateToLocal(this.getUnLocalizedName());
    }

    ResourceLocation getTextureLocation();

    int getColor();

    float getHardness();

    int getResistance();

    int getHarvestLevel();

    @Nonnull
    String getHarvestTool();

    void getDrops(int fortune, List<ItemStack> itemStacks);

    void setRecipes(List<IRecipe> recipes);

    String getOreDict();

    @Nullable
    CreativeTabs getCreativeTab();

    @Nullable
    IGeneratedModel getGeneratedModel();

    Material getMaterial();

    @Nonnull
    ItemStack getItemStack();
    
    @Nonnull
    IBlockState getBlockState();
    
    void setMeta(int x);
    
    void setBlock(Block block);

    boolean isSideSolid(EnumFacing side);

    boolean isTopSolid();

    BlockFaceShape getBlockFaceShape();

    AxisAlignedBB getBoundingBox();

    boolean isFullCube();

    boolean isOpaqueCube();

    boolean isPassable();

    boolean isFullBlock();
	
    boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon);

    int getLightOpacity();

    boolean canSilkHarvest();

    void onNeighborChange(World world, BlockPos pos, Block block, BlockPos fromPos);

    boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos);

    boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player);

	default BlockRenderLayer getRenderLayer() {
		return isOpaqueCube() ? BlockRenderLayer.SOLID : BlockRenderLayer.CUTOUT;
	}
}
