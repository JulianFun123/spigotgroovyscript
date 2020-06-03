# SpigotGroovyScript (SpigotGS)
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

You can also type `spigotgroovyscript create test` into the console and a Plugin structure will be created.

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

### Inventory Example
```groovy
def fluffyBed = createItem(Material.BED).setDisplayName("A fluffy bed!").build();

addCommand("testinventory") {
    if (sender instanceof Player) {
           Player player = (Player) sender;
           def inventory = createInventory(9, "Hallo")
           inventory.setItem(0, fluffyBed)
           player.openInventory(inventory)
    }
}

onInventoryClick {
    if (event.currentItem.equals(fluffyBed)) {
        event.whoClicked.sendMessage 'Amazing!'
    } else {
        event.whoClicked.sendMessage 'This is not a fluffy bed :('
    } 
}
```