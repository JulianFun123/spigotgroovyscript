package xyz.gojani.julian.minecraft.spigotgroovyscript

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.reflections.Reflections
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.CommandUtils

import java.lang.reflect.Method

class SpigotGroovyScriptPlugin extends JavaPlugin {

    static SpigotGroovyScriptPlugin instance

    void onEnable() {
        instance = this

        if (!new File(getDataFolder(), 'plugins').exists())
            new File(getDataFolder(), 'plugins').mkdir()

        if (!new File(getDataFolder(), 'libraries').exists())
            new File(getDataFolder(), 'libraries').mkdir()

        config.addDefault('plugins', [:])
        config.options().copyDefaults(true)
        saveConfig()

        Bukkit.consoleSender.sendMessage("§aStarted SpigotGroovyPlugin")



        GroovyScriptEngine engine = new GroovyScriptEngine(getDataFolder().getPath()+'/plugins')
        Binding binding = new Binding()
        binding.plugin = this

        Reflections reflections = new Reflections('org.bukkit.event')
        reflections.getSubTypesOf(Event.class).each {
            print('on'+it.simpleName.substring(0, it.simpleName.length()-5))
            binding['on'+it.simpleName.substring(0, it.simpleName.length()-5)] = { Closure handler ->
                Bukkit.pluginManager.registerEvent(it, new Listener() {}, EventPriority.NORMAL, (Listener listener, Event event) -> {
                    handler.setProperty('event', event)
                    handler(event)
                } , this)
            }
        }

        binding.addCommand = { String name, Closure handler ->
            CommandUtils.registerCommand(name)
            getCommand(name).setExecutor((CommandSender sender, Command command, String alias, String[] args)->{
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

        binding.onDisable = this.&onDisable

        URLClassLoader urlClassLoader = (URLClassLoader) SpigotGroovyScriptPlugin.class.getClassLoader()

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class)
        method.setAccessible(true)
        new File(getDataFolder(), "libraries").listFiles().each {
            Bukkit.consoleSender.sendMessage("§aImporting library §c"+it.name)
            method.invoke(urlClassLoader, it.toURI().toURL())
        }

        binding.broadcast = Bukkit.&broadcastMessage

        ImportCustomizer importCustomizer = new ImportCustomizer()

        initImports(importCustomizer)

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.addCompilationCustomizers(importCustomizer)
        engine.setConfig(compilerConfiguration)
        Gson gson = new GsonBuilder().setPrettyPrinting().create()


        new File(getDataFolder(), 'plugins').listFiles().each {
            PluginConfig pluginConfig = gson.fromJson(new File(it, "config.json").text, PluginConfig.class)
            Bukkit.consoleSender.sendMessage("§aStarting plugin §c"+pluginConfig.name+" §b"+pluginConfig.name+"/"+pluginConfig.main+".groovy")
            engine.createScript(it.getName()+"/"+pluginConfig.main+".groovy", binding).run()
        }
    }

    static void initImports(ImportCustomizer importCustomizer){
        importCustomizer.addStarImports("org.bukkit",
                "org.bukkit.event",
                "org.bukkit.event.block",
                "org.bukkit.event.enchantment",
                "org.bukkit.event.entity",
                "org.bukkit.event.hanging",
                "org.bukkit.event.inventory",
                "org.bukkit.event.painting",
                "org.bukkit.event.player",
                "org.bukkit.event.server",
                "org.bukkit.event.vehicle",
                "org.bukkit.event.weather",
                "org.bukkit.event.world",
                "org.bukkit.block",
                "org.bukkit.inventory",
                "org.bukkit.scheduler",
                "org.bukkit.scoreboard",
                "org.bukkit.material")
    }

    void onDisable() {

    }


}
