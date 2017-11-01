package libs.RssParser

import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.io.StringReader
import java.util.ArrayList
import java.util.Date
import java.util.Observable

/**
 * Created by Marco Gomiero on 12/02/2015.
 */
class XMLParser : Observable()
{
	private val articles: ArrayList<Article> = ArrayList()
	internal var currentArticle: Article

	init
	{

		currentArticle = Article()
	}

	fun parseXML(xml: String)
	{
		try
		{
			val factory = XmlPullParserFactory.newInstance()

			factory.isNamespaceAware = false
			val xmlPullParser = factory.newPullParser()

			xmlPullParser.setInput(StringReader(xml))
			var insideItem = false
			var eventType = xmlPullParser.eventType

			while (eventType != XmlPullParser.END_DOCUMENT)
			{

				if (eventType == XmlPullParser.START_TAG)
				{

					if (xmlPullParser.name.equals("item", ignoreCase = true))
					{

						insideItem = true

					}
					else if (xmlPullParser.name.equals("title", ignoreCase = true))
					{

						if (insideItem)
						{

							val title = xmlPullParser.nextText()
							currentArticle.title = title
						}

					}
					else if (xmlPullParser.name.equals("NewsFeed", ignoreCase = true))
					{
						if (insideItem)
						{

							val newsFeed = xmlPullParser.nextText()
							currentArticle.newsFeed = newsFeed
						}

					}
					else if (xmlPullParser.name.equals("link", ignoreCase = true))
					{

						if (insideItem)
						{

							val link = xmlPullParser.nextText()
							currentArticle.link = link
						}

					}
					else if (xmlPullParser.name.equals("dc:creator", ignoreCase = true))
					{

						if (insideItem)
						{

							val author = xmlPullParser.nextText()
							currentArticle.author = author
						}

					}
					else if (xmlPullParser.name.equals("content:encoded", ignoreCase = true))
					{

						if (insideItem)
						{

							val htmlData = xmlPullParser.nextText()
							val doc = Jsoup.parse(htmlData)
							try
							{

								val pic = doc.select("img").first().attr("abs:src")
								currentArticle.image = pic

							}
							catch (e: NullPointerException)
							{

								currentArticle.image = null

							}

							currentArticle.content = htmlData
						}

					}
					else if (xmlPullParser.name.equals("description", ignoreCase = true))
					{

						if (insideItem)
						{

							val description = xmlPullParser.nextText()
							currentArticle.description = description
						}

					}
					else if (xmlPullParser.name.equals("pubDate", ignoreCase = true))
					{
						val dateText = xmlPullParser.nextText()
						val pubDate = DateTime(dateText)
						currentArticle.pubDate = pubDate
					}

				}
				else if (eventType == XmlPullParser.END_TAG && xmlPullParser.name.equals("item", ignoreCase = true))
				{

					insideItem = false
					articles.add(currentArticle)
					currentArticle = Article()
				}
				eventType = xmlPullParser.next()
			}

			triggerObserver()

		}
		catch (e: Exception)
		{

			e.printStackTrace()

		}

	}

	private fun triggerObserver()
	{
		setChanged()
		notifyObservers(articles)
	}
}