package com.javadude.speech.language

object Key: MoveableItemImpl("It is a shiny brass key")
object Letter: MoveableItemImpl("It reads You win!")
object Safe: ContainerItemImpl(
    description = "It is a very heavy locked box. There is a keyhole on it",
    locked = true,
    open = false,
    key = Key,
    items = listOf(Letter)
)

object Porch: Room(
    description = "This is the front porch of your house",
    north = {Hall},
)

object Bedroom: Room(
    description = "This is where you sleep",
    west = {Hall}
)

object Study: Room(
    description = "This is where you pretend to work",
    south = {Hall},
    items = listOf(Safe)
)

object Kitchen: Room(
    description = "This is a room you never use and aren't really certain why you even have it.",
    east = {Hall},
    items = listOf(Key)
)
object Hall: Room(
    description= "This is the main hall of your house",
    north = {Study},
    south = {Porch},
    east = {Bedroom},
    west = {Kitchen}
)
