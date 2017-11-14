package com.jakelauer.baseballtheater.experiences.news

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.CardView
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.AdapterChildItem
import com.jakelauer.baseballtheater.base.ItemViewHolder
import libs.ButterKnife.bindView
import libs.RssParser.Article
import org.joda.time.DateTime


/**
 * Created by Jake on 10/30/2017.
 */
class ArticleItem(data: ArticleItem.Data) : AdapterChildItem<ArticleItem.Data, ArticleItem.ViewHolder>(data)
{
	override fun getLayoutResId() = R.layout.article_item

	override fun createViewHolder(view: View) = ViewHolder(view)

	fun setSeen()
	{
		m_data.m_seen = true
	}

	override fun onBindView(viewHolder: ViewHolder, context: Context)
	{
		viewHolder.m_card.alpha = if (m_data.m_seen) 0.5f else 1f

		val image = m_data.m_article.image;
		if (image != null)
		{
			viewHolder.m_thumbnail.visibility = View.VISIBLE
			viewHolder.m_thumbnail.loadUrl(image)
		}
		else
		{
			viewHolder.m_thumbnail.visibility = View.GONE
		}

		if (m_data.m_article.title != null)
		{
			viewHolder.m_title.text = m_data.m_article.title
		}

		if (m_data.m_article.description != null && m_data.m_article.description?.indexOf("[comments]") == -1)
		{
			viewHolder.m_subtitle.text = Html.fromHtml(m_data.m_article.description)
		}

		val newsFeed = m_data.m_article.newsFeed
		if (newsFeed != null)
		{
			val source = NewsFeeds.valueOf(newsFeed)

			val sourceString = when (source)
			{
				NewsFeeds.mtr -> "MLB Trade Rumors"
				NewsFeeds.espn -> "ESPN"
				NewsFeeds.fivethirtyeight -> "538"
				NewsFeeds.fangraphs -> "FanGraphs"
				NewsFeeds.mlb -> "MLB.com"
				NewsFeeds.reddit -> "Reddit"
			}

			viewHolder.m_source.text = "Source: $sourceString"
		}

		val linkUri = Uri.parse(m_data.m_article.link)

		viewHolder.m_metaIcon.loadUrl(getFaviconUrl(linkUri))

		viewHolder.m_timeAgo.text = getTimeAgo(m_data.m_article.pubDate)
	}

	fun getFaviconUrl(uri: Uri): String
	{
		return "http://s2.googleusercontent.com/s2/favicons?domain_url=${uri.host}"
	}

	fun getTimeAgo(dateTime: DateTime?): String?
	{
		if (dateTime == null)
		{
			return null
		}

		var time = dateTime.millis
		if (time < 1000000000000L)
		{
			// if timestamp given in seconds, convert to millis
			time *= 1000
		}

		val now = DateTime.now().millis
		if (time > now || time <= 0)
		{
			return null
		}

		val SECOND_MILLIS = 1000;
		val MINUTE_MILLIS = 60 * SECOND_MILLIS
		val HOUR_MILLIS = 60 * MINUTE_MILLIS
		val DAY_MILLIS = 24 * HOUR_MILLIS

		val diff = now - time
		return when
		{
			diff < MINUTE_MILLIS -> "just now"
			diff < 2 * MINUTE_MILLIS -> "a minute ago"
			diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
			diff < 90 * MINUTE_MILLIS -> "an hour ago"
			diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
			diff < 48 * HOUR_MILLIS -> "yesterday"
			else -> "${(diff / DAY_MILLIS)} days ago"
		}
	}

	class Data(val m_article: Article, var m_seen: Boolean)

	class ViewHolder(view: View) : ItemViewHolder(view)
	{
		var m_card: CardView by bindView(R.id.ARTICLE_card)
		var m_timeAgo: TextView by bindView(R.id.ARTICLE_time_ago)
		var m_metaIcon: ImageView by bindView(R.id.ARTICLE_meta_icon)
		var m_source: TextView by bindView(R.id.ARTICLE_source)
		var m_thumbnail: ImageView by bindView(R.id.ARTICLE_thumbnail)
		var m_title: TextView by bindView(R.id.ARTICLE_title)
		var m_subtitle: TextView by bindView(R.id.ARTICLE_subtitle)
	}
}

