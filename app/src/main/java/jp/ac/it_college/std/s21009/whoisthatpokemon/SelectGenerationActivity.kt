package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableRow
import com.google.gson.Gson
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.ActivitySelectGenerationBinding
import jp.ac.it_college.std.s21009.whoisthatpokemon.model.pokedex.Pokedex
import jp.ac.it_college.std.s21009.whoisthatpokemon.model.pokedex.PokedexJson
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.reflect.KClass

class SelectGenerationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectGenerationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenerationBinding.inflate(layoutInflater)
        title = getString(R.string.select_generation)

        setContentView(binding.root)
        parseJson("filtered_pokedex.json", PokedexJson::class).pokedex.map { it.name }
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

    private fun <T : Any> parseJson(file: String, c: KClass<T>): T {
        val inputStream = assets.open(file)
        val jsonReader = InputStreamReader(inputStream, "UTF-8").readText()
        inputStream.close()
        return Gson().fromJson(jsonReader, c.java)
    }
}