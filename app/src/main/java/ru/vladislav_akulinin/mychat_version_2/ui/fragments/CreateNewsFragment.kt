package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_news.view.*

import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.News
import ru.vladislav_akulinin.mychat_version_2.utils.Utils.hideKeyboard

class CreateNewsFragment : Fragment() {
    var firebaseReference = FirebaseDatabase.getInstance().reference.child("News")

    private lateinit var textSubject: String
    private lateinit var textNews: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_create_news, container, false)

        view.button_create_news_create_news.setOnClickListener{
            textSubject = view.edit_text_subject_create_news.text.toString()
            textNews = view.edit_text_text_create_news.text.toString()

            val news = News("$textSubject", "$textNews")
            firebaseReference.push().setValue(news)

            fragmentManager!!
                    .beginTransaction()
                    .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                    .replace(R.id.container, NewsFragment())
                    .addToBackStack(null)
                    .commit()

            hideKeyboard()
        }

        return view
    }

}
