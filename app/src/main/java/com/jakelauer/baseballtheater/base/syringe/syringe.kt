package com.jakelauer.baseballtheater.base.syringe

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.jakelauer.baseballtheater.base.BaseFragment
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.full.starProjectedType

/**
 * Created by Jake on 10/25/2017.
 */

@Suppress("UNCHECKED_CAST")
class syringe<T>
{
	operator fun getValue(activity: Activity, property: KProperty<*>): T =
			activity.intent.extras[property.name] as T

	operator fun setValue(activity: Activity, property: KProperty<*>, value: T)
	{
	}

	operator fun getValue(fragment: Fragment, property: KProperty<*>): T =
			fragment.arguments.get(property.name) as T

	operator fun setValue(activity: Fragment, property: KProperty<*>, value: T)
	{
	}

	companion object
	{
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
											args.put(BaseFragment.INJECT_MEMBER_PREFIX + name, value)
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
}