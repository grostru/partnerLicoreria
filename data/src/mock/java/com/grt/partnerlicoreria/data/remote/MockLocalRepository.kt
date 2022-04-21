package com.grt.pokemon.data.remote

import android.content.Context
import com.grt.pokemon.data.UtilsPokemons
import com.grt.pokemon.domain.model.PokemonModel
import com.grt.pokemon.domain.repository.EditPokemonsRepository
import com.grt.pokemon.domain.repository.GetPokemonsRepository
import com.squareup.picasso.Picasso
import java.util.*

class MockLocalRepository(private val context: Context) : EditPokemonsRepository, GetPokemonsRepository {

    private val map : SortedMap<Int,PokemonModel> = sortedMapOf()

    override suspend fun deletePokemon(pokemonModel: PokemonModel) {
       map.remove(pokemonModel.id.toInt())
    }

    override suspend fun deletePokemons() {
        map.clear()
    }

    override suspend fun savePokemons(vararg pokemons: PokemonModel) {
        pokemons.forEach { pok ->
            val imageBitmap = Picasso.with(context).load(pok.url_image_default).get()
            val pathImage = UtilsPokemons.saveToInternalStorage(pok.name,imageBitmap,context)
            val pokCopy= pok.copy(url_image_default = pathImage)

            map[pok.id.toInt()] = pokCopy
      }
    }

    override suspend fun modifyPokemon(pokemonModel: PokemonModel) {
        map[pokemonModel.id] = pokemonModel
    }

    override suspend fun getPokemons(): Result<List<PokemonModel>> {
        var listPokemons = map.values.toList()
        
       return Result.success(listPokemons.sortedBy {
           it.name
       })
    }

}