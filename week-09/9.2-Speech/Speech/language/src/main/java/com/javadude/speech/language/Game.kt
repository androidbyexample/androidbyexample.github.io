package com.javadude.speech.language

object Inventory : Container {
    override val description: String get() = "Your Inventory"
    override val items = mutableMapOf<String, Item>()
    override val itemListPrefix: String
        get() = "Your inventory contains"
    override fun getFullDescription() =
        when {
            isEmpty -> "Your inventory is empty"
            else -> itemNamesOrEmpty()
        }
}

class CommandException(message: String): RuntimeException(message)

class Game(
    private val reporter: (String) -> Unit,
    private val onQuit: () -> Unit
) {
    var currentRoom: Room = Porch
        private set
    var command: String = "look"

    fun report(message: String) = reporter(message)

    fun drop(itemName: String) {
        move(
            item = Inventory.find(itemName),
            from = Inventory,
            to = currentRoom
        )
        report("You dropped the $itemName")
    }

    fun get(itemName: String) {
        getFrom(itemName, currentRoom)
        report("You picked up the $itemName")
    }

    fun getFrom(itemName: String, containerName: String) {
        getFrom(itemName, currentRoom.findContainerItem(containerName))
        report("You picked up the $itemName from the $containerName")
    }

    private fun getFrom(itemName: String, container: Container) =
        if (itemName in Inventory) {
            throw CommandException("You already have the $itemName")
        } else {
            move(
                item = container.find(itemName),
                from = container,
                to = Inventory
            )
        }

    fun open(containerName: String) =
        currentRoom.findContainerItem(containerName).open(this)

    fun close(containerName: String) =
        currentRoom.findContainerItem(containerName).close(this)

    fun unlock(containerName: String) =
        currentRoom.findContainerItem(containerName).unlock(this)

    fun lock(containerName: String) =
        currentRoom.findContainerItem(containerName).lock(this)

    fun examine(itemName: String) {
        val item = currentRoom.findOrNull(itemName) ?: Inventory.findOrNull(itemName) ?:
            throw CommandException("You don't see the $itemName in the room or your inventory")
        report(item.getFullDescription())
    }

    fun quit() = onQuit()

    fun look() = report(currentRoom.getFullDescription())

    fun getLocationInfo() = "Location: " + currentRoom.name + "\n\nCommand: " + command

    fun inventory() = report(Inventory.getFullDescription())

    fun go(direction: Direction) {
        currentRoom = currentRoom.move(direction) ?: throw CommandException("You cannot go ${direction.name}")
        look()
    }

}

fun move(item: Item, from: Container, to: Container) {
    if (item !is MoveableItem) {
        throw CommandException("You can't pick up the ${item.name}")
    }
    from.remove(item)
    to.add(item)
}
