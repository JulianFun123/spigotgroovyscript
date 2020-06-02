package xyz.gojani.julian.minecraft.spigotgroovyscript

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class SpigotGroovyScriptPlugin extends JavaPlugin {

    void onEnable() {
        config.addDefault('plugins', [:])
        config.options().copyDefaults(true)
        saveConfig()

        Bukkit.consoleSender.sendMessage("§aStarted SpigotGroovyPlugin")

        GroovyScriptEngine engine = new GroovyScriptEngine('plugins/SpigotGroovyScript/scripts')
        Binding binding = new Binding()
        binding.plugin = this

        ImportCustomizer importCustomizer = new ImportCustomizer()

        initImports(importCustomizer)

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.addCompilationCustomizers(importCustomizer)
        engine.setConfig(compilerConfiguration)
        Gson gson = new GsonBuilder().setPrettyPrinting().create()

        new File('plugins/SpigotGroovyScript/scripts').listFiles().each {
            PluginConfig pluginConfig = gson.fromJson(new File(it, "config.json").text, PluginConfig.class)
            Bukkit.consoleSender.sendMessage("§aStarting plugin §c"+pluginConfig.name+" §b"+pluginConfig.name+"/"+pluginConfig.main+".groovy")
            engine.createScript(pluginConfig.name+"/"+pluginConfig.main+".groovy", binding).run()
        }
    }

    void initImports(ImportCustomizer importCustomizer){
        importCustomizer.addImport('Bukkit', 'org.bukkit.Bukkit')
        importCustomizer.addImport('bukkit.Listener', 'org.bukkit.event.Listener')
    }

    void onDisable() {

    }
}
