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

		val argumentTypes = argList.map { arg ->
			arg.javaClass.kotlin.starProjectedType
		}

		// Loop through all the constructors for this class
		for (constructor in javaClass.kotlin.constructors)
		{
			// Ignore constructors with no parameters
			if (constructor.parameters.isNotEmpty())
			{
				val constructorParamTypes = constructor.parameters.map { param -> param.type }

				// If this constructor has the same number of params as the argument list...
				if (constructorParamTypes.size == argumentTypes.size)
				{
					// Check to make sure they are all the same types as each other
					var allEqual = true
					for (i in 0 until argumentTypes.size)
					{
						val paramType = constructorParamTypes[i]
						if (!argumentTypes.contains(paramType))
						{
							allEqual = false
							break
						}
					}

					// If they are, add them to the bundle as "m_" + parameterName
					if (allEqual)
					{
						for (i in 0 until argumentTypes.size)
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