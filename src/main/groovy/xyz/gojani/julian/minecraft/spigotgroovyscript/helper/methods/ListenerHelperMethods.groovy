package xyz.gojani.julian.minecraft.spigotgroovyscript.helper.methods

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.reflections.Reflections
import xyz.gojani.julian.minecraft.spigotgroovyscript.SpigotGroovyScriptPlugin

class ListenerHelperMethods {
    static void initialize(Binding binding) {
        Reflections reflections = new Reflections('org.bukkit.event')
        reflections.getSubTypesOf(Event.class).each {
            binding['on'+it.simpleName.substring(0, it.simpleName.length()-5)] = { Closure handler ->
                Bukkit.pluginManager.registerEvent(it, new Listener() {}, EventPriority.NORMAL, (Listener listener, Event event) -> {
                    handler.setProperty('event', event)
                    handler(event)
                } , SpigotGroovyScriptPlugin.instance)
            }
        }
    }

}
