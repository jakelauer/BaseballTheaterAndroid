package com.jakelauer.baseballtheater.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView


/**
 * Created by jlauer on 2/14/2018.
 */
class NoScrollListView : ListView
{
	constructor(context: Context): super(context)

	constructor(context: Context, attrs: AttributeSet): super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)

	public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		val params = layoutParams
		params.height = measuredHeight
	}
}