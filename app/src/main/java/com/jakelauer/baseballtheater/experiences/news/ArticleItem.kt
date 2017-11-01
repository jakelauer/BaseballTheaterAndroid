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
import libs.ButterKnife.bindView
import libs.RssParser.Article
import android.support.customtabs.CustomTabsIntent




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
		val image = m_data.image;
		if (image != null)
		{
			viewHolder.m_thumbnail.visibility = View.VISIBLE
			viewHolder.m_thumbnail.loadUrl(image)
		}
		else
		{
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

		val newsFeed = m_data.newsFeed
		if (newsFeed != null)
		{
			var source = NewsFeeds.valueOf(newsFeed)

			when (source)
			{
				NewsFeeds.espn -> "ESPN"
				NewsFeeds.si -> "Sports Illustrated"
				NewsFeeds.fivethirtyeight -> "538"
				NewsFeeds.fangraphs -> "FanGraphs"
				NewsFeeds.mlb -> "MLB.com"
			}

			viewHolder.m_source.text = "Source: $source"
		}

		viewHolder.m_wrapper.setOnClickListener {
			val builder = CustomTabsIntent.Builder()
			val customTabsIntent = builder.build()
			customTabsIntent.launchUrl(context, Uri.parse(m_data.link))
		}
	}

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_wrapper: View by bindView(R.id.ARTICLE_wrapper)
		var m_source: TextView by bindView(R.id.ARTICLE_source)
		var m_thumbnail: ImageView by bindView(R.id.ARTICLE_thumbnail)
		var m_title: TextView by bindView(R.id.ARTICLE_title)
		var m_subtitle: TextView by bindView(R.id.ARTICLE_subtitle)
	}
}