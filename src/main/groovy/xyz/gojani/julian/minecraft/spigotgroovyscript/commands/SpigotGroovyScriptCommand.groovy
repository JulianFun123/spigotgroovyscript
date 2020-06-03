package xyz.gojani.julian.minecraft.spigotgroovyscript.commands

import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import xyz.gojani.julian.minecraft.spigotgroovyscript.PluginConfig
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin

class SpigotGroovyScriptCommand implements CommandExecutor{
    @Override
    boolean onCommand(CommandSender sender, Command command, String s, String[] args) {


        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create") && args.length == 2) {
                    println 'Creating...'
                    File pluginDirectory = new File(SpigotGroovyScriptPlugin.instance.dataFolder, "plugins/"+args[1])
                    pluginDirectory.mkdir()
                    File config = new File(pluginDirectory, "config.json")
                    config.createNewFile()
                    String classFileName = args[1].substring(0, 1).toUpperCase() + args[1].substring(1)
                    config.text = new GsonBuilder().setPrettyPrinting().create().toJson(new PluginConfig(args[1], classFileName))
                    File classFile = new File(pluginDirectory, classFileName+".groovy")
                    classFile.createNewFile()
                    classFile.text = "package "+args[1]+
                                     "\nprintln 'Plugin started!'"
                    println 'Done!'
                    return false
                }
            }
        }

        println 'Command "'+args[0]+'" not found '+args.length

        false
    }
}
