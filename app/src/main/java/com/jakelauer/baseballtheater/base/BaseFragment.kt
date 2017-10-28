package com.jakelauer.baseballtheater.base

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import icepick.Icepick
import java.io.Serializable
import kotlin.reflect.full.starProjectedType

/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseFragment<TData : Any> : Fragment
{
	companion object
	{
		val INJECT_MEMBER_PREFIX = "m_"
	}

	@LayoutRes
	abstract fun getLayoutResourceId(): Int

	private lateinit var m_model: TData

	fun getModel() = m_model

	constructor() : super()

	constructor(vararg argList: Any)
	{
		val args = Bundle()

		val types = argList.map { arg ->
			arg.javaClass.kotlin.starProjectedType
		}

		for (constructor in javaClass.kotlin.constructors)
		{
			if (constructor.parameters.size > 0)
			{
				val paramTypes = constructor.parameters.map { param -> param.type }

				if (paramTypes.size == types.size)
				{
					var allEqual = true
					for (i in 0 until types.size)
					{
						val paramType = paramTypes[i]
						if (!types.contains(paramType))
						{
							allEqual = false
							break
						}
					}

					if (allEqual)
					{
						for (i in 0 until types.size)
						{
							val parameter = constructor.parameters[i]
							val name = parameter.name
							val value = argList[0]
							if (name != null)
							{
								args.put(INJECT_MEMBER_PREFIX + name, value)
							}
						}
					}
				}
			}
		}

		arguments = args
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		Icepick.restoreInstanceState(this, savedInstanceState)
		super.onCreate(savedInstanceState)

		m_model = createModel()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
	{
		var view = inflater.inflate(getLayoutResourceId(), container, false)

		return view
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		beforeOnBindView()
		onBindView()

		loadData()
	}

	protected abstract fun createModel(): TData

	protected open fun onBindView()
	{

	}

	protected abstract fun loadData()

	open protected fun beforeOnBindView()
	{

	}

	fun Bundle.put(key: String, value: Any)
	{
		when (value)
		{
			is String -> this.putString(key, value)
			is Int -> this.putInt(key, value)
			is Parcelable -> this.putParcelable(key, value)
			is Serializable -> this.putSerializable(key, value)
			else -> throw TypeCastException("Type not allowed in Bundles")
		}
	}
}