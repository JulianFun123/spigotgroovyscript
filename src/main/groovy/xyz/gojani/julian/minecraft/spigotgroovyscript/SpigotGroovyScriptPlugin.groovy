package xyz.gojani.julian.minecraft.spigotgroovyscript

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import xyz.gojani.julian.minecraft.spigotgroovyscript.commands.SpigotGroovyScriptCommand
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods.CommandHelperMethods
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods.InventoryHelperMethods
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods.ListenerHelperMethods
import xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods.PlayerHelperMethods
import xyz.gojani.julian.minecraft.spigotgroovyscript.listener.ClickHandlerListener

import java.lang.reflect.Method

class SpigotGroovyScriptPlugin extends JavaPlugin {

    static SpigotGroovyScriptPlugin instance
    private List<Closure> onDisableHandler

    void onEnable() {
        instance = this

        if (!new File(getDataFolder(), 'plugins').exists())
            new File(getDataFolder(), 'plugins').mkdir()

        if (!new File(getDataFolder(), 'libraries').exists())
            new File(getDataFolder(), 'libraries').mkdir()

        saveConfig()

        Bukkit.getPluginManager().registerEvents(new ClickHandlerListener(), this)

        getCommand("spigotgroovyscript").setExecutor(new SpigotGroovyScriptCommand())
        getCommand("spigotgs").setExecutor(new SpigotGroovyScriptCommand())

        Bukkit.consoleSender.sendMessage("§aStarted SpigotGroovyPlugin")


        GroovyScriptEngine engine = new GroovyScriptEngine(getDataFolder().getPath()+'/plugins')
        Binding binding = new Binding()
        binding.plugin = this
        CommandHelperMethods.initialize(binding)
        PlayerHelperMethods.initialize(binding)
        ListenerHelperMethods.initialize(binding)
        InventoryHelperMethods.initialize(binding)

        onDisableHandler = []
        binding.onDisable = { Closure handler ->
            onDisableHandler.add(handler)
        }

        def onInitializedHandler = []
        binding.onInitialized = { Closure handler ->
            onInitializedHandler.add(handler)
        }

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

        onInitializedHandler.each {
            it()
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
                "org.bukkit.material",
                "org.bukkit.entity")
    }

    void onDisable() {
        onDisableHandler.each {
            it()
        }
    }
}
