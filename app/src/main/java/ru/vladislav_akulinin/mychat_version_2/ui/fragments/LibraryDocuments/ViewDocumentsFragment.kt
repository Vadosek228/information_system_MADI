package ru.vladislav_akulinin.mychat_version_2.ui.fragments.LibraryDocuments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_view_documents.view.*

import ru.vladislav_akulinin.mychat_version_2.R

class ViewDocumentsFragment(getUrl: String) : Fragment() {
    val url = getUrl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_documents, container, false)

        getDataWebView(view)

        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getDataWebView(view: View) {
        view.web_view_documents.settings?.javaScriptEnabled = true
        view.web_view_documents.loadUrl(url)
        view.web_view_documents.setInitialScale(1)
        view.web_view_documents.settings?.builtInZoomControls = true
        view.web_view_documents.settings?.displayZoomControls = false
        view.web_view_documents.settings?.loadWithOverviewMode = true
        view.web_view_documents.settings?.useWideViewPort = true
    }
}
