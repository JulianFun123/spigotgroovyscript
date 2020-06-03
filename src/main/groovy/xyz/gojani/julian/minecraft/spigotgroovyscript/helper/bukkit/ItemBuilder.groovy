package xyz.gojani.julian.minecraft.spigotgroovyscript.helper.bukkit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin

class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;


    ItemBuilder(Material material, int amount, int duration) {
        itemStack = new ItemStack(material, amount, (short) duration);
        itemMeta = itemStack.getItemMeta();
    }

    ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.getItemMeta();
    }

    ItemBuilder setMaterial(Material material) {
        itemStack.setType(material);
        return this;
    }

    ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    ItemBuilder setDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    ItemBuilder setLore(String[] lines) {

        ArrayList<String> lores = new ArrayList<String>();
        for (String line : lines)
            lores.add(line);
        itemMeta.setLore(lores);
        return this;
    }

    ItemBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }


    ItemBuilder addEnchant(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    ItemStack getItemStack(){
        itemStack
    }

    ItemMeta getItemMeta(){
        itemMeta
    }


    ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}