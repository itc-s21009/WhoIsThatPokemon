package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        title = getString(R.string.app_name)
        setContentView(binding.root)
        initJsonData(assets)
        onBackPressedDispatcher.addCallback(this) {
            val controller = binding.fragmentContainerView.findNavController()
            when (controller.currentDestination?.id) {
                R.id.selectGenerationFragment -> controller.navigate(
                    SelectGenerationFragmentDirections.selectToTitle()
                )
                R.id.quizFragment -> controller.navigate(
                    QuizFragmentDirections.quizToTitle()
                )
            }
        }
    }

}