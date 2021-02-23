package edu.utap.freecycle.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.utap.freecycle.MainActivity
import edu.utap.freecycle.MainViewModel
import edu.utap.freecycle.R
import kotlinx.android.synthetic.main.fragment_login_screen.*


class LoginScreen : Fragment() {

    //private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_button.setOnClickListener {
            (activity as MainActivity?)?.fetchNewToken()
        }
    }

}