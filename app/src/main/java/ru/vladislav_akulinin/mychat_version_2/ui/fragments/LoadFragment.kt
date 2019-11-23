package ru.vladislav_akulinin.mychat_version_2.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.JsonObject
import ru.vladislav_akulinin.mychat_version_2.R
import kotlinx.android.synthetic.main.fragment_load.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.IOException

class LoadFragment : Fragment() {

    /*
    * через веб вие сделать поиск таблицы,
    * если таблица появилась запарсить ее,
    * а затем сохранить расписание или нет!
    * */

    private var list: List<String>? = null

    companion object {
        fun newInstance(): LoadFragment = LoadFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_load, container, false)

        list = ArrayList()

        getDataWebView(view)

        val button = view.findViewById<Button>(ru.vladislav_akulinin.mychat_version_2.R.id.button_get_timetable_load)
        button.setOnClickListener {
            GlobalScope.launch {
                getData()
            }
        }

        return view
    }

    private fun getData() {
        try {
//            val url = "https://jack911.pythonanywhere.com/api/v1.0/tplan1?gp_name=2мБД2"
//
//
//            val jObject = JSONObject(loadJSONFromAsset())
//
//            Log.e("test", jObject.toString())


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val is_ = context?.assets?.open("info_group.json")

            val size = is_?.available()

            val buffer = size?.let { ByteArray(it) }

            is_?.read(buffer)

            is_?.close()

            json = buffer?.let { String(it) }



        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getDataWebView(view: View) {
        view.web_view_library.settings?.javaScriptEnabled = true
        view.web_view_library.loadUrl("http://tplan.madi.ru/r/?task=7")
        view.web_view_library.setInitialScale(1)
        view.web_view_library.settings?.builtInZoomControls = true
        view.web_view_library.settings?.displayZoomControls = false
        view.web_view_library.settings?.loadWithOverviewMode = true
        view.web_view_library.settings?.useWideViewPort = true
    }

}
