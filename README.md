This is a spigot server plugin that disables mods that display the health of other players (such as [neat](http://minecraftsix.com/neat-mod/)).

This works by replacing the health value of every [ENTITY_METADATA](http://wiki.vg/Entities#Living) packet with the maximum health of that entity.
The plugin was inspired by [AntiDamageIndicators](https://github.com/libraryaddict/AntiDamageIndicators), but works with spigot 1.9+.

This plugin depends on [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/).
