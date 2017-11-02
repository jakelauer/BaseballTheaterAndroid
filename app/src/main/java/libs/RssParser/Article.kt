/*
*   Copyright 2016 Marco Gomiero
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
*/

package libs.RssParser

import org.joda.time.DateTime
import java.util.*

/**
 * Created by Marco Gomiero on 12/02/2015.
 */
class Article
{
	var newsFeed: String? = null
	var title: String? = null
	var author: String? = null
	var link: String? = null
	var pubDate: DateTime? = null
	var description: String? = null
	var content: String? = null
	var image: String? = null

	override fun toString(): String
	{
		return "Article{" +
				"title='" + title + '\'' +
				", author='" + author + '\'' +
				", link='" + link + '\'' +
				", pubDate=" + pubDate +
				", description='" + description + '\'' +
				", content='" + content + '\'' +
				", image='" + image + '\'' +
				'}'
	}

}