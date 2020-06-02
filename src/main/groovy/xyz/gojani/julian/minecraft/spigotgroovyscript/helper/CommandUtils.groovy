package xyz.gojani.julian.minecraft.spigotgroovyscript.helper

import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin


import java.lang.reflect.Constructor
import java.lang.reflect.Field

class CommandUtils {
    static void registerCommand(String... aliases) {
        PluginCommand command = getCommand(aliases[0], SpigotGroovyScriptPlugin.instance);

        command.setAliases(Arrays.asList(aliases));
        getCommandMap().register(SpigotGroovyScriptPlugin.instance.getDescription().getName(), command);
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandMap;
    }
}
