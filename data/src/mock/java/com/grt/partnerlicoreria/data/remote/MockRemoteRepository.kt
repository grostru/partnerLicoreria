package com.grt.pokemon.data.remote

import android.content.Context
import com.grt.pokemon.data.UtilsPokemons
import com.grt.pokemon.data.database.room.toDataDB
import com.grt.pokemon.domain.model.PokemonModel
import com.grt.pokemon.domain.repository.GetPokemonsRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MockRemoteRepository()  : GetPokemonsRepository {

    override suspend fun getPokemons(): Result<List<PokemonModel>> {
        delay(2000L)
        val listPokemon = mutableListOf<PokemonModel>()
        var i = 1
        while (i < 45){
            val factor = if (i % 2 == 0)
                1
            else
                -1

            val pok = PokemonModel(
                id = i,
                name = "Name: $i",
                weight = i,
                height = i,
                base_experience = i,
                is_default = i % 2 == 0,
                forms = "Forma: $i",
                species = "Especie: $i",
                url_image_default="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$i.png",
                favorite = i % 2 == 0,
                abilities = "Ability: $i \n Ability2: $i \n",
                types = "Type: $i \n Type2: $i \n"
            )

            listPokemon.add(pok)
            i++
        }

        return Result.success(listPokemon)
        //return Result.failure(Throwable("Se ha producido un error"))
    }
}