package tk.jacobempire.cloakpaneladdon.init;

import com.swdteam.common.item.BaseBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.cloakpaneladdon.block.CloakingPanelBlock;
import tk.jacobempire.cloakpaneladdon.block.ShieldPanelBlock;

import java.util.function.Supplier;

import static tk.jacobempire.cloakpaneladdon.CloakPanelAddon.MODID;

public class Blocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, Item.Properties properties, boolean needsItem) {
        RegistryObject<Block> blockObj = BLOCKS.register(name, block);
        if (needsItem) {
            Items.ITEMS.register(name, () -> {
                return new BaseBlockItem((Block)blockObj.get(), properties);
            });
        }

        return blockObj;
    }

    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name) {
        return registerBlock(block, name, new Item.Properties(), true);
    }

    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, boolean needsItem) {
        return registerBlock(block, name, new Item.Properties(), needsItem);
    }

    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, Item.Properties properties) {
        return registerBlock(block, name, properties, true);
    }

    public static <B extends Block> RegistryObject<Block> registerBlock(Supplier<B> block, String name, ItemGroup itemgroup) {
        return registerBlock(block, name, (new Item.Properties()).tab(itemgroup), true);
    }

    public static RegistryObject<Block> CLOAKING_PANEL = registerBlock(() ->
            new CloakingPanelBlock(AbstractBlock.Properties.of(Material.STONE).strength(6.25F, 5.75F).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE)),
            "cloaking_panel", ItemGroup.TAB_BUILDING_BLOCKS);

    public static RegistryObject<Block> SHIELD_PANEL = registerBlock(() ->
                    new ShieldPanelBlock(AbstractBlock.Properties.of(Material.STONE).strength(6.25F, 5.75F).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE)),
            "shield_panel", ItemGroup.TAB_BUILDING_BLOCKS);
}
