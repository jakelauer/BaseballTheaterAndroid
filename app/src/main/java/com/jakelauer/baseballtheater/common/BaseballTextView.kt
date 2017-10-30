package com.jakelauer.baseballtheater.common

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.jakelauer.baseballtheater.base.FontCache


/**
 * Created by Jake on 10/24/2017.
 */
class BaseballTextView : TextView
{
	constructor(context: Context) : super(context)
	{
		setCustomFont()
	}

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	{
		setCustomFont()
	}

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	{
		setCustomFont()
	}

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
			: super(context, attrs, defStyleAttr, defStyleRes)
	{
		setCustomFont()
	}

	fun setCustomFont()
	{
		val customFont = FontCache.getTypeface("bebas.ttf", context)
		typeface = customFont
	}
}