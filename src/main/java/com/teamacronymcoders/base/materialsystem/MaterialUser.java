package com.teamacronymcoders.base.materialsystem;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teamacronymcoders.base.IBaseMod;
import com.teamacronymcoders.base.materialsystem.items.ItemMaterialPart;
import com.teamacronymcoders.base.materialsystem.materialparts.MaterialPart;
import com.teamacronymcoders.base.materialsystem.materialparts.MaterialPartSave;
import com.teamacronymcoders.base.materialsystem.materials.Material;
import com.teamacronymcoders.base.materialsystem.parts.Part;
import com.teamacronymcoders.base.registrysystem.ItemRegistry;
import com.teamacronymcoders.base.savesystem.SaveLoader;
import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MaterialUser {
    private final IBaseMod mod;

    private Map<String, Integer> nameMapping = Maps.newHashMap();
    private int nextId = 0;
    private BiMap<Integer, MaterialPart> materialPartBiMap = HashBiMap.create();

    public MaterialUser(IBaseMod mod) {
        this.mod = mod;
    }

    public void setup() {
        nameMapping.putAll(SaveLoader.getSavedObject("material_parts_" + mod.getID(), MaterialPartSave.class).getMaterialMappings());
        nameMapping.values().forEach(id -> {
            if (id > nextId) {
                nextId = id + 1;
            }
        });
    }

    public void finishUp() {
        MaterialPartSave save;

        List<MaterialPart> parts = Lists.newArrayList(materialPartBiMap.values());
        for (MaterialPart materialPart : parts) {
            try {
                materialPart.getData().validate();
            } catch (MaterialException e) {
                this.logError("MaterialPart with name " + materialPart.getLocalizedName() +
                        " had an error when validating data. Error: " + e.getMessage());
            }
            materialPart.setup();
        }

        if (hasErred()) {
            this.logError("Found Errors with Material System Loading. Saving Back up");
            save = MaterialPartSave.of(nameMapping);
        } else {
            save = MaterialPartSave.of(materialPartBiMap.inverse().entrySet().stream()
                    .map(entry -> Pair.of(entry.getKey().getUnlocalizedName(), entry.getValue()))
                    .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));
        }
        SaveLoader.saveObject("material_parts_" + mod.getID(), save);
    }

    protected boolean hasErred() {
        return false;
    }

    public IBaseMod getMod() {
        return mod;
    }

    public MaterialPart getMaterialPart(int itemDamage) {
        return materialPartBiMap.getOrDefault(itemDamage, MaterialSystem.MISSING_MATERIAL_PART);
    }

    public int getMaterialPartId(MaterialPart materialPart) {
        return materialPartBiMap.inverse().get(materialPart);
    }

    @SuppressWarnings("UnusedReturnValue")
    public List<MaterialPart> registerPartsForMaterial(Material material, String... partNames) {
        List<MaterialPart> materialParts = Lists.newArrayList();
        for (String partName : partNames) {
            Part part = MaterialSystem.getPart(partName);
            if (part != null) {
                MaterialPart materialPart = new MaterialPart(this, material, part);
                this.registerMaterialPart(materialPart);
                materialParts.add(materialPart);
            } else {
                this.logError("Could not find part " + partName + " for " + material.getName());
            }
        }
        return materialParts;
    }

    public void registerMaterialPart(MaterialPart materialPart) {
        int id;
        if (nameMapping.containsKey(materialPart.getUnlocalizedName())) {
            id = nameMapping.get(materialPart.getUnlocalizedName());
        } else {
            id = nextId++;
        }
        if (!MaterialSystem.hasMaterialPart(materialPart)) {
            materialPartBiMap.put(id, materialPart);
            MaterialSystem.registerMaterialPart( materialPart);
        } else {
            this.logError(materialPart.getUnlocalizedName() + " is already registered.");

        }
    }

    public void registerItem(Item item) {
        item.setCreativeTab(MaterialSystem.materialCreativeTab);
        mod.getRegistryHolder().getRegistry(ItemRegistry.class, "ITEM").register(item);

    }

    public String getId() {
        return this.getMod().getID();
    }

    public void logError(String errorMessage) {
        this.getMod().getLogger().warning(errorMessage);
    }
}
