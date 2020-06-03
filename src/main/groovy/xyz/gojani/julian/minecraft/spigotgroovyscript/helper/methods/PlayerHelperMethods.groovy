package xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin;

class PlayerHelperMethods {

    static void initialize(Binding binding){
        binding.eachPlayer = this.&eachPlayer

        Player.metaClass.sendPlayer = { server ->
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("pvp");
            delegate.sendPluginMessage(SpigotGroovyScriptPlugin.instance, "BungeeCord", out.toByteArray());
        }
    }

    static Closure eachPlayer = { Closure handler ->
        Bukkit.getOnlinePlayers().each { player ->
            handler.setProperty("player", player)
            handler(player)
        }
    }

}
