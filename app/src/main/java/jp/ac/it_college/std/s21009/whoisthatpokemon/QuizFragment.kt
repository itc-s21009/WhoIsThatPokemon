package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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
        binding.tvQuestionCount.text = getString(R.string.question_count, args.questionNumber)
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
        class ClickListener(val correct: Boolean) : View.OnClickListener {
            override fun onClick(v: View) {
                Toast.makeText(v.context, if (correct) "正解ですーーー" else "不正解", Toast.LENGTH_SHORT)
                    .show()
                val selectedPokemonName = (v as Button).text.toString()
                val correctPokemonName = buttons[0].text.toString()
                args.selectedAnswers[args.questionNumber - 1] = selectedPokemonName
                args.correctAnswers[args.questionNumber - 1] = correctPokemonName
                Navigation.findNavController(v).navigate(
                    if (args.questionNumber >= 10) {
                        QuizFragmentDirections.quizToResult(
                            args.selectedAnswers,
                            args.correctAnswers
                        )
                    } else {
                        QuizFragmentDirections.quizToQuiz(
                            pokemonIdList,
                            args.selectedAnswers,
                            args.correctAnswers
                        ).apply {
                            correctCount = args.correctCount + if (correct) 1 else 0
                            questionNumber = args.questionNumber + 1
                        }
                    }
                )
            }
        }
        buttons[0].setOnClickListener(ClickListener(true))
        buttons[1].setOnClickListener(ClickListener(false))
        buttons[2].setOnClickListener(ClickListener(false))
        buttons[3].setOnClickListener(ClickListener(false))
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