package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.ActivitySelectGenerationBinding
import jp.ac.it_college.std.s21009.whoisthatpokemon.model.pokedex.PokedexJson

class SelectGenerationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectGenerationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenerationBinding.inflate(layoutInflater)
        title = getString(R.string.select_generation)

        setContentView(binding.root)
        parseJson(assets, "filtered_pokedex.json", PokedexJson::class).pokedex.map { it.name }
            .forEach { name ->
                val button = Button(this).apply { text = name }
                button.setOnClickListener {
                    startActivity(Intent(this, QuizActivity::class.java).apply {
                        putExtra("generation", name)
                    })
                }
                binding.generations.addView(button)
            }
    }
}