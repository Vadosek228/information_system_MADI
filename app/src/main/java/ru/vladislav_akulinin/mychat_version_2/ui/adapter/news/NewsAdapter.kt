package ru.vladislav_akulinin.mychat_version_2.ui.adapter.news

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.vladislav_akulinin.mychat_version_2.R
import ru.vladislav_akulinin.mychat_version_2.model.News
import ru.vladislav_akulinin.mychat_version_2.model.User

class NewsAdapter(
        internal var context: Context?
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

//    private var clickListener: OnItemClickedListener? = null
    private var newsList: MutableList<News> = ArrayList()

//    fun registerOnItemCallBack(clickListener: OnItemClickedListener) {
//        this.clickListener = clickListener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun getItemCount(): Int = newsList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.subject.text = newsList[position].subject
        holder.test.text = newsList[position].text
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var subject: TextView = itemView.findViewById(R.id.text_view_subject)
        var test: TextView = itemView.findViewById(R.id.text_view_text)

    }

    fun addAll(news: List<News>) {
        val init = newsList.size
        newsList.clear()
        newsList.addAll(news)
        notifyItemRangeChanged(init, news.size)
    }

    fun removeLastItem() {
        newsList.removeAt(newsList.size - 1)
    }
}