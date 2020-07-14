package com.teamacronymcoders.base.materialsystem.parttype;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.teamacronymcoders.base.materialsystem.MaterialSystem;
import com.teamacronymcoders.base.materialsystem.MaterialUser;
import com.teamacronymcoders.base.materialsystem.blocks.SubBlockPart;
import com.teamacronymcoders.base.materialsystem.materialparts.MaterialPart;
import com.teamacronymcoders.base.subblocksystem.SubBlockSystem;
import com.teamacronymcoders.base.subblocksystem.blocks.ISubBlock;

import net.minecraft.item.ItemStack;

public class BlockPartType extends PartType {
    public BlockPartType() {
        this("block");
    }

    public BlockPartType(String name) {
        this(name, new ArrayList<>());
    }

    public BlockPartType(String name, List<PartDataPiece> dataPieces) {
        super(name, setupData(dataPieces));
    }

    private static List<PartDataPiece> setupData(List<PartDataPiece> blockDataPieces) {
        blockDataPieces.add(new PartDataPiece("hardness", false));
        blockDataPieces.add(new PartDataPiece("resistance", false));
        blockDataPieces.add(new PartDataPiece("harvestLevel", false));
        blockDataPieces.add(new PartDataPiece("harvestTool", false));
        blockDataPieces.add(new PartDataPiece("transparent", false));
        blockDataPieces.add(new PartDataPiece("beaconBase", false));
        return blockDataPieces;
    }

    @Override
    public void setup(@Nonnull MaterialPart materialPart, @Nonnull MaterialUser materialUser) {
        registerSubBlock(materialPart, new SubBlockPart(materialPart, MaterialSystem.materialCreativeTab));
    }

    public void registerSubBlock(MaterialPart materialPart, ISubBlock subBlock) {
        SubBlockSystem subBlockSystem = materialPart.getMaterialUser().getMod().getSubBlockSystem();
        if (subBlockSystem != null) {
            subBlockSystem.registerSubBlock(subBlock);
        }
    }

    @Override
    public ItemStack getItemStack(MaterialPart materialPart) {
        SubBlockSystem subBlockSystem = materialPart.getMaterialUser().getMod().getSubBlockSystem();
        ItemStack itemStack = ItemStack.EMPTY;
        if (subBlockSystem != null) {
            itemStack = subBlockSystem.getSubBlock(materialPart.getUnlocalizedName()).getItemStack();
        }
        return itemStack;
    }
}
