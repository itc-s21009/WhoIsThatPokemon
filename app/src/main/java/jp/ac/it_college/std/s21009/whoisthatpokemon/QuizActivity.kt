package jp.ac.it_college.std.s21009.whoisthatpokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val generation = intent.getStringExtra("generation")
        title = generation
    }
}