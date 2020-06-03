package xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods


import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.bukkit.ItemBuilder

class InventoryHelperMethods {

    static void initialize(Binding binding){
        binding.createInventory = this.&createInventory
        binding.createItem = this.&createItem
        binding.fromItem = this.&fromItem
    }

    private static Closure createInventory = { int size, String name ->
        Bukkit.createInventory(null, size, name)
    }

    private static Closure createItem = { Material material ->
        new ItemBuilder(material)
    }

    private static Closure fromItem = { ItemStack itemStack ->
        new ItemBuilder(itemStack)
    }



}
