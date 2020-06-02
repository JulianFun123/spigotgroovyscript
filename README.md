# SpigotGroovyScript
## A Simplified way to create Spigot-Plugins


### Example command
```groovy
addCommand("hello") {
    sender.sendMessage "Hello, "+sender.name
}
```

### Example Event
```groovy
onPlayerJoin {
    event.player.sendMessage "Hello, "+event.player.name
}
```