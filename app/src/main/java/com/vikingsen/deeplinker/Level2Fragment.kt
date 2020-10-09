package com.vikingsen.deeplinker

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.vikingsen.deeplinker.databinding.Level2FragmentBinding

class Level2Fragment : Fragment() {

    private var _binding: Level2FragmentBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<Level2FragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = Level2FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.level2TextView.text = getString(R.string.code, args.code2)

        if (arguments?.containsKey(NavController.KEY_DEEP_LINK_INTENT) == true) {
            Log.d("Level2Fragment", "FROM DEEPLINK")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // You have to use because this method lives outside of the View scope.
        _binding?.level2TextView
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun getElevatorUri(code: String): Uri {
            return "deeplinker://elevator/level2/$code".toUri()
        }

        fun getOutsideUri(code1: String, code2: String): Uri {
            return "deeplinker://building/level1/$code1/level2/$code2".toUri()
        }
    }
}