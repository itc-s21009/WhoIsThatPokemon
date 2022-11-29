package jp.ac.it_college.std.s21009.whoisthatpokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
        val listData = mutableListOf<ResultData>()
        for (i in args.selectedAnswers.indices) {
            if (args.selectedAnswers[i] == args.correctAnswers[i]) {
                count++
            }
            listData.add(
                ResultData(
                    args.selectedAnswers[i],
                    args.correctAnswers[i],
                    args.pokemonImages[i]
                )
            )
        }
        binding.rvResult.apply {
            layoutManager = LinearLayoutManager(activity).apply {
                addItemDecoration(DividerItemDecoration(activity, orientation))
            }
            adapter = ResultAdapter(listData)
        }
        binding.btTitle.setOnClickListener{
            Navigation.findNavController(it).navigate(
                ResultFragmentDirections.resultToTitle()
            )
        }
        return binding.root
    }
}