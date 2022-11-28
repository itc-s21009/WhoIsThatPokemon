package jp.ac.it_college.std.s21009.whoisthatpokemon.model.pokedex

data class PokedexJson(
    val pokedex: List<Pokedex>
)

data class Pokedex(
    val id: Int,
    val name: String,
    val entries: List<Entry>
)

data class Entry(
    val entry_number: Int,
    val pokemon_id: Int
)