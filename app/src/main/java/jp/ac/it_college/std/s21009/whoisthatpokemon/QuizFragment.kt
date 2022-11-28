package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.FragmentQuizBinding
import jp.ac.it_college.std.s21009.whoisthatpokemon.model.PokemonImageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val args: QuizFragmentArgs by navArgs()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val BASE_URL = "https://pokeapi.co/api/v2/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemonIdList = args.pokemonIdList
        val answerPokemonId = pokemonIdList[Random().nextInt(pokemonIdList.size)]
        showPokemonImage(answerPokemonId)
        val buttons = listOf(
            binding.answer1,
            binding.answer2,
            binding.answer3,
            binding.answer4,
        ).shuffled()
        val selectedIdList = mutableListOf<Int>()
        selectedIdList.add(answerPokemonId)
        var i = 0
        while (i < 3) {
            val selectedId = pokemonIdList[Random().nextInt(pokemonIdList.size)]
            if (!selectedIdList.contains(selectedId)) {
                selectedIdList.add(selectedId)
                i++
            }
        }
        for (i in 0..3) {
            buttons[i].text = pokemon.filter { p -> p.id == selectedIdList[i] }[0].name
        }
        buttons[0].setOnClickListener { Toast.makeText(activity, "正解ですーーー", Toast.LENGTH_SHORT).show() }
        buttons[1].setOnClickListener { Toast.makeText(activity, "不正解", Toast.LENGTH_SHORT).show() }
        buttons[2].setOnClickListener { Toast.makeText(activity, "不正解", Toast.LENGTH_SHORT).show() }
        buttons[3].setOnClickListener { Toast.makeText(activity, "不正解", Toast.LENGTH_SHORT).show() }
    }

    @UiThread
    private fun showPokemonImage(id: Int) {
        lifecycleScope.launch {
            val info = getPokemonImage(id)
            val url = info.sprites.other.officialArtwork.frontDefault
            Picasso.get().load(url).into(binding.imgPokemon)
        }
    }

    @WorkerThread
    private suspend fun getPokemonImage(id: Int): PokemonImageInfo {
        return withContext(Dispatchers.IO) {
            val retrofit = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(MoshiConverterFactory.create(moshi))
            }.build()
            val service = retrofit.create(PokemonService::class.java)
            service.fetchPokemon(id).execute().body() ?: throw IllegalStateException("ポケモンが見つかりません")
        }
    }

    interface PokemonService {
        @GET("pokemon/{id}/")
        fun fetchPokemon(@Path("id") id: Int): Call<PokemonImageInfo>
    }

}