# SpigotGroovyScript
## A Simplified way to create Spigot-Plugins

### Getting started
#### Your first test project
Put the [plugin](https://github.com/JulianFun123/spigotgroovyscript/releases) into the plugins folder and reload the server. 2 folders will be created. 
In the plugins/SpigotGroovyScript/plugins folder you can create a folder that is named `test` with a `config.json` file in it.
The `config.json` has to look like this:
```json
{
    "name": "test",
    "main": "Test"
}
```
create a `Test.groovy` and start to code!


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