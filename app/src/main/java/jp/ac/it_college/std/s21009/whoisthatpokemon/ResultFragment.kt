package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import jp.ac.it_college.std.s21009.whoisthatpokemon.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        var count = 0
        for (i in args.selectedAnswers.indices) {
            if (args.selectedAnswers[i] == args.correctAnswers[i]) {
                count++
            }
        }
        Log.i("testlog", "correctAnswers(${args.correctAnswers.size}): ${args.correctAnswers.joinToString(",")}")
        Log.i("testlog", "selectedAnswers(${args.selectedAnswers.size}): ${args.selectedAnswers.joinToString(",")}")
        Log.i("testlog", "正解数: ${count}問")
        return binding.root
    }

}