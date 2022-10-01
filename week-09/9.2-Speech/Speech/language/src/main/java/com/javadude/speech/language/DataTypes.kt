package com.javadude.speech.language

sealed interface Thing

interface Named: Thing {
    val name: String get() = this::class.simpleName.toString().lowercase()
    val description: String
    fun getFullDescription() = description
}

interface Item: Named
interface MoveableItem: Item

interface Container: Named {
    val items: MutableMap<String, Item>
    val isEmpty: Boolean
        get() = items.isEmpty()
    val itemNames: Set<String>
        get() = items.keys

    override fun getFullDescription() =
        when {
            isEmpty -> "$description\n\nThe $name is empty"
            else -> "$description\n\n${itemNamesOrEmpty()}"
        }

    val itemListPrefix: String
    fun itemNamesOrEmpty() =
        if (items.isEmpty()) "" else "\n\n${itemListPrefix}\n\n${items.keys.joinToString(separator = "\n") { "\ta $it"}}"

    fun add(item: Item): Boolean {
        items[item.name] = item
        return true
    }

    fun remove(item: Item) = items.remove(item.name)
    fun remove(name: String) = items.remove(name)
    operator fun contains(item: Item?) = item?.name in items.keys
    operator fun contains(itemName: String) = itemName in items.keys

    fun find(itemName: String) =
        items[itemName] ?: throw CommandException("You don't see the $itemName in the $name")

    fun findOrNull(itemName: String) = items[itemName]

    fun findContainerItem(itemName: String) =
        find(itemName) as? ContainerItem ?: throw CommandException("$itemName cannot contain anything")
}

abstract class ContainerItemImpl(
    override val description: String,
    override var locked: Boolean,
    override var open: Boolean,
    override val key: Item? = null,
    items: List<Item>,
) : ContainerItem {
    override val items = items.map { it.name to it }.toMap().toMutableMap()
}

interface ContainerItem: Item, Container {
    var locked: Boolean
    var open: Boolean
    val key: Item?

    override val itemListPrefix: String
        get() = "The $name contains"

    private fun <T> ifOpen(block: () -> T) =
        if (!open) {
            throw CommandException("The $name is not open")
        } else {
            block()
        }

    override fun find(itemName: String) =
        ifOpen<Item> {
            items[itemName] ?: throw CommandException("You don't see the $itemName in the $name")
        }

    override fun getFullDescription() =
        when {
            locked -> "$description\n\nThe $name is locked"
            !open -> "$description\n\nThe $name is closed"
            else -> "$description\n\nThe $name is open\n\n${itemNamesOrEmpty()}"
        }

    fun open(game: Game) =
        when {
            open -> throw CommandException("The $name is already open")
            locked -> throw CommandException("The $name is locked")
            else -> {
                open = true
                game.report("You open the $name")
            }
        }

    fun close(game: Game) =
        when {
            !open -> throw CommandException("The $name is already closed")
            else -> {
                open = false
                game.report("You close the $name")
            }
        }

    fun lock(game: Game) =
        when {
            key == null -> throw CommandException("The $name cannot be locked")
            open -> throw CommandException("The $name is already open")
            locked -> throw CommandException("The $name is already locked")
            key !in Inventory -> throw CommandException("You do not have the key for the $name")
            else -> {
                locked = true
                game.report("You lock the $name")
            }
        }

    fun unlock(game: Game) =
        when {
            !locked -> throw CommandException("The $name is not locked")
            key !in Inventory -> throw CommandException("You do not have the key for this container")
            else -> {
                locked = false
                game.report("You unlock the $name")
            }
        }
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

abstract class Room(
    override val description: String,
    val north: (()->Room)? = null,
    val south: (()->Room)? = null,
    val east: (()->Room)? = null,
    val west: (()->Room)? = null,
    items: List<Item> = emptyList(),
): Container {
    override val items =
        items.map {
            it.name to it
        }.toMap().toMutableMap()

    override val itemListPrefix: String
        get() = "You see"

    private fun pathNamesOrEmpty(): String {
        val paths = listOfNotNull(
            north?.let {Direction.NORTH to it()},
            south?.let {Direction.SOUTH to it()},
            east?.let {Direction.EAST to it()},
            west?.let {Direction.WEST to it()},
        ).toMap()
        return if (paths.isEmpty()) "" else "\n\nYou can go ${paths.keys.joinToString { it.name }}"
    }

    override fun getFullDescription() = """
            |$description
            |${pathNamesOrEmpty()}
            |${itemNamesOrEmpty()}
        """.trimMargin()

    fun move(direction: Direction): Room? {
        val paths = listOfNotNull(
            north?.let {Direction.NORTH to it()},
            south?.let {Direction.SOUTH to it()},
            east?.let {Direction.EAST to it()},
            west?.let {Direction.WEST to it()},
        ).toMap()
        return paths[direction]
    }
}

abstract class MoveableItemImpl(
    override val description: String,
): MoveableItem