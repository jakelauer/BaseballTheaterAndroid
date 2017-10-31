package com.jakelauer.baseballtheater.experiences.news

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import com.prof.rssparser.Article
import libs.bindView


/**
 * Created by Jake on 10/30/2017.
 */
class ArticleItem(data: Article, activity: Activity) : AdapterChildItem<Article, ArticleItem.ViewHolder>(data)
{
	private var m_activity = activity

	override fun getLayoutResId() = R.layout.article_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		if (m_data.image != null)
		{
			viewHolder.m_thumbnail.visibility = View.VISIBLE
			viewHolder.m_thumbnail.loadUrl(m_data.image)
		}
		else{
			viewHolder.m_thumbnail.visibility = View.GONE
		}

		if (m_data.title != null)
		{
			viewHolder.m_title.text = m_data.title
		}

		if (m_data.description != null)
		{
			viewHolder.m_subtitle.text = m_data.description
		}

		viewHolder.m_wrapper.setOnClickListener{
			val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(m_data.link))
			ContextCompat.startActivity(m_activity, browserIntent, null)
		}
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_wrapper: View by bindView(R.id.ARTICLE_wrapper)
		var m_thumbnail: ImageView by bindView(R.id.ARTICLE_thumbnail)
		var m_title: TextView by bindView(R.id.ARTICLE_title)
		var m_subtitle: TextView by bindView(R.id.ARTICLE_subtitle)
	}
}