package com.jakelauer.baseballtheater.base.syringe

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.starProjectedType

/**
 * Created by Jake on 10/25/2017.
 */

fun <A : Any> Fragment.syringe()
		: ReadWriteProperty<Fragment, A> = required(m_fragmentArgFinder)

fun <A : Any> Activity.syringe()
		: ReadWriteProperty<Activity, A> = required(m_activityExtraFinder)

@Suppress("UNCHECKED_CAST")
private fun <T, V : Any> required(finder: T.(String) -> Any?)
		= Lazy { t: T, desc -> t.finder(desc.name) as V }

private val m_fragmentArgFinder: Fragment.(String) -> Any?
	get() = { arguments[it] }

private val m_activityExtraFinder: Activity.(String) -> Any?
	get() = { intent.extras[it] }


open class Lazy<in T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadWriteProperty<T, V>
{
	private object EMPTY

	private var value: Any? = EMPTY

	override fun getValue(thisRef: T, property: KProperty<*>): V
	{
		if (value == EMPTY)
		{
			value = initializer(thisRef, property)
		}
		@Suppress("UNCHECKED_CAST")
		return value as V
	}

	override fun setValue(thisRef: T, property: KProperty<*>, value: V)
	{
	}
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

class Syringe
{
	companion object
	{
		val INJECT_MEMBER_PREFIX = "m_"

		fun inject(fragment: Fragment, vararg argList: Any)
		{
			return FragmentBundler.doInjection(fragment, *argList)
		}

		class FragmentBundler
		{
			companion object
			{
				fun doInjection(fragment: Fragment, vararg argList: Any)
				{
					val args = Bundle()

					val argumentTypes = argList.map { arg ->
						arg.javaClass.kotlin.starProjectedType
					}

					// Loop through all the constructors for this class
					for (constructor in fragment.javaClass.kotlin.constructors)
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

					fragment.arguments = args
				}
			}
		}
	}
}