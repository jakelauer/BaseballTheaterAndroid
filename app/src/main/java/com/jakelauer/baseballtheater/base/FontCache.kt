package com.jakelauer.baseballtheater.base

import android.content.Context
import android.graphics.Typeface

/**
 * Created by Jake on 10/24/2017.
 */


class FontCache
{
	companion object
	{
		private val fontCache = HashMap<String, Typeface>()

		fun getTypeface(fontname: String, context: Context): Typeface?
		{
			var typeface = fontCache.get(fontname)

			if (typeface == null)
			{
				try
				{
					typeface = Typeface.createFromAsset(context.assets, "fonts/" + fontname)
				}
				catch (e: Exception)
				{
					return null
				}

				fontCache.put(fontname, typeface)
			}

			return typeface
		}
	}
}