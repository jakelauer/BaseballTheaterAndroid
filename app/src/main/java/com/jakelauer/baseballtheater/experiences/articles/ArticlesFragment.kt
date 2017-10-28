package com.jakelauer.baseballtheater.experiences.articles

import com.jakelauer.baseballtheater.R
import com.jakelauer.baseballtheater.base.FlexibleListFragment

/**
 * Created by Jake on 10/27/2017.
 */

class ArticlesFragment : FlexibleListFragment<ArticlesFragment.Model>
{
	constructor() : super()

	override fun getLayoutResourceId() = R.layout.articles_fragment

	override fun createModel() = Model()

	override fun loadData()
	{
	}

	class Model
}