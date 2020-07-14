package com.teamacronymcoders.base.materialsystem.blocks;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.*;
import com.teamacronymcoders.base.materialsystem.materialparts.MaterialPart;
import com.teamacronymcoders.base.materialsystem.partdata.DataPartParsers;
import com.teamacronymcoders.base.materialsystem.partdata.MaterialPartData;
import com.teamacronymcoders.base.materialsystem.parts.Part;
import com.teamacronymcoders.base.subblocksystem.blocks.SubBlockBase;
import com.teamacronymcoders.base.util.files.templates.TemplateFile;
import com.teamacronymcoders.base.util.files.templates.TemplateManager;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SubBlockPart extends SubBlockBase {
    private MaterialPart materialPart;
    private CreativeTabs creativeTabs;

    private float hardness = 5;
    private int resistance = 30;
    private int harvestLevel = 1;
    private String harvestTool = "pickaxe";
    private boolean transparent = false;
    private boolean beaconBase = false;

    public SubBlockPart(MaterialPart materialPart, CreativeTabs creativeTab) {
        super(materialPart.getUnlocalizedName());
        this.materialPart = materialPart;
        MaterialPartData data = this.materialPart.getData();

        hardness = data.getValue("hardness", hardness, DataPartParsers::getFloat);
        resistance = data.getValue("resistance", resistance, DataPartParsers::getInt);
        harvestLevel = data.getValue("harvestLevel", harvestLevel, DataPartParsers::getInt);
        harvestTool = data.getValue("harvestTool", harvestTool, DataPartParsers::getString);
        transparent = data.getValue("transparent", transparent, DataPartParsers::getBool);
        beaconBase = data.getValue("beaconBase", beaconBase, DataPartParsers::getBool);

        this.creativeTabs = creativeTab;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
    }

    public void setHarvestTool(String harvestTool) {
        this.harvestTool = harvestTool;
    }

    @Override
    public String getLocalizedName() {
        return this.materialPart.getLocalizedName();
    }

    @Override
    public String getUnLocalizedName() {
        return this.materialPart.getUnlocalizedName();
    }

    @Override
    public int getColor() {
        return materialPart.getColor();
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public int getResistance() {
        return resistance;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public String getHarvestTool() {
        return harvestTool;
    }

    @Override
    public String getOreDict() {
        return this.materialPart.getOreDictString();
    }

    @Nullable
    @Override
    public CreativeTabs getCreativeTab() {
        return creativeTabs;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        ResourceLocation location = materialPart.getTextureLocation();
        return new ResourceLocation(location.getNamespace(), this.getModelPrefix() + this.getUnLocalizedName());
    }

    @Override
    public IGeneratedModel getGeneratedModel() {
        TemplateFile templateFile = TemplateManager.getTemplateFile("colored_block");
        Map<String, String> replacements = Maps.newHashMap();

        Part part = materialPart.getPart();
        replacements.put("texture", String.format("%s:blocks/%s", part.getOwnerId(), part.getShortUnlocalizedName()));
        templateFile.replaceContents(replacements);

        return new GeneratedModel(this.getModelPrefix() + this.getUnLocalizedName(), ModelType.BLOCKSTATE,
                templateFile.getFileContents());
    }

    @Override
    protected String getModelPrefix() {
        return "materials/";
    }

    public MaterialPart getMaterialPart() {
        return materialPart;
    }

    @Override
    public boolean isSideSolid(EnumFacing side) {
        return transparent;
    }

    @Override
    public boolean isTopSolid() {
        return transparent;
    }

    @Override
    public BlockFaceShape getBlockFaceShape() {
        return BlockFaceShape.SOLID;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return Block.FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube() {
        return !transparent;
    }

    @Override
    public boolean isOpaqueCube() {
        return !transparent;
    }
    
    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return !transparent;
    }
    
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return beaconBase;
    }

    @Override
    public int getLightOpacity() {
        return transparent ? 255 : 0;
    }

    @Override
    public boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player) {
        return false;
    }
}
