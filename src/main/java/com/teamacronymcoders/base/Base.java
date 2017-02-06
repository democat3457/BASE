package com.teamacronymcoders.base;

import com.teamacronymcoders.base.materialsystem.MaterialsSystem;
import com.teamacronymcoders.base.proxies.ModCommonProxy;
import com.teamacronymcoders.base.util.LanguageHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.teamacronymcoders.base.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = "[" + MINECRAFT_VERSION + "]",
        dependencies = DEPENDENCIES)
public class Base extends BaseModFoundation<Base> {
    public static final LanguageHelper languageHelper = new LanguageHelper(MODID);

    @Instance(MODID)
    public static Base instance;

    @SidedProxy(clientSide = "com.teamacronymcoders.base.proxies.ModClientProxy", serverSide = "com.teamacronymcoders.base.proxies.ModCommonProxy")
    public static ModCommonProxy proxy;

    public Base() {
        super(MODID, NAME, VERSION, CreativeTabs.MISC);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MaterialsSystem.initParts();
        super.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void setCreativeTab(CreativeTabs creativeTab) {
        this.creativeTab = creativeTab;
    }

    @Override
    public Base getInstance() {
        return instance;
    }
}
