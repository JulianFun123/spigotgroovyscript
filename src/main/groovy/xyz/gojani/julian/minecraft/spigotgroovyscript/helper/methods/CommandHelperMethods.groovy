package xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.CommandUtils;

class CommandHelperMethods {
    static void initialize(Binding binding){
        binding.addCommand = this.&addCommand
    }

    static Closure addCommand = { String name, Closure handler ->
        CommandUtils.registerCommand(name)
        SpigotGroovyScriptPlugin.instance.getCommand(name).setExecutor((CommandSender sender, Command command, String alias, String[] args)->{
            handler.setProperty("sender", sender)
            handler.setProperty("command", command)
            handler.setProperty("alias", alias)
            handler.setProperty("args", args)
            if (handler.getParameterTypes().length > 1)
                handler(sender, args)
            else
                handler()

            false
        })
    }
}
