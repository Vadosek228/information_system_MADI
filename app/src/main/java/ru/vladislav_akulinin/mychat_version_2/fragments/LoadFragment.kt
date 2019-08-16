package ru.vladislav_akulinin.mychat_version_2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.vladislav_akulinin.mychat_version_2.R

class LoadFragment : Fragment() {

    companion object {
        fun newInstance(): LoadFragment = LoadFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load, container, false)
    }


}
