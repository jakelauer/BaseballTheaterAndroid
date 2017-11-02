package com.jakelauer.baseballtheater.base.syringe

import android.app.Activity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.design.internal.ParcelableSparseArray
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Size
import android.util.SizeF
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

/**
 * Created by Jake on 10/25/2017.
 */

fun <A : Any> Fragment.syringe()
		: ReadWriteProperty<Fragment, A> = required(m_fragmentArgFinder)

fun <A : Any?> Fragment.syringeOptional()
		: ReadWriteProperty<Fragment, A?> = optional(m_fragmentArgOptionalFinder)

fun <A : Any> Activity.syringe()
		: ReadWriteProperty<Activity, A> = required(m_activityExtraFinder)

fun <A : Any> Activity.syringeOptional()
		: ReadWriteProperty<Activity, A> = optional(m_activityExtraOptionalFinder)

@Suppress("UNCHECKED_CAST")
private fun <T, V : Any> required(finder: T.(String) -> Any)
		= Lazy { t: T, desc -> t.finder(desc.name) as V }

@Suppress("UNCHECKED_CAST")
private fun <T, V : Any?> optional(finder: T.(String) -> Any?)
		= Lazy { t: T, desc -> t.finder(desc.name) as V }

private val m_fragmentArgFinder: Fragment.(String) -> Any
	get() = { arguments[it] ?: notFound(it) }

private val m_activityExtraFinder: Activity.(String) -> Any
	get() = { intent.extras[it] ?: notFound(it) }

private val m_fragmentArgOptionalFinder: Fragment.(String) -> Any?
	get() = { arguments[it] }

private val m_activityExtraOptionalFinder: Activity.(String) -> Any?
	get() = { intent.extras[it] }

private fun notFound(name: String)
{
	throw IllegalArgumentException("No argument found by name $name")
}


open class Lazy<in T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadWriteProperty<T, V>
{
	private object EMPTY

	private var value: Any? = EMPTY

	override fun getValue(thisRef: T, property: KProperty<*>): V
	{
		value = initializer(thisRef, property)

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
		is Array<*> -> this.putSerializable(key, value)
		is ArrayList<*> -> this.putSerializable(key, value)
		is Byte -> this.putByte(key, value)
		is ByteArray -> this.putByteArray(key, value)
		is Char -> this.putChar(key, value)
		is CharArray -> this.putCharArray(key, value)
		is CharSequence -> this.putCharSequence(key, value)
		is Float -> this.putFloat(key, value)
		is IBinder -> this.putBinder(key, value)
		is Int -> this.putInt(key, value)
		is Long -> this.putLong(key, value)
		is Parcelable -> this.putParcelable(key, value)
		is ParcelableSparseArray -> this.putSparseParcelableArray(key, value)
		is Serializable -> this.putSerializable(key, value)
		is Short -> this.putShort(key, value)
		is ShortArray -> this.putShortArray(key, value)
		is Size -> this.putSize(key, value)
		is SizeF -> this.putSizeF(key, value)
		is String -> this.putString(key, value)
		else -> throw TypeCastException("Type not allowed in Bundles")
	}
}

class Syringe
{
	companion object
	{
		var INJECT_MEMBER_PREFIX = "m_"
			private set

		var FRAGMENT_CONSTRUCTOR_CACHE: HashMap<String, List<String?>> = HashMap()

		fun setup(injectMemberPrefix: String = "m_")
		{
			INJECT_MEMBER_PREFIX = injectMemberPrefix
		}

		fun inject(fragment: Fragment, argList: Array<out Any>)
		{
			FragmentBundler.doInjection(fragment, argList)
		}

		class FragmentBundler
		{
			companion object
			{
				fun doInjection(fragment: Fragment, argList: Array<out Any>)
				{
					val startTime = System.nanoTime()
					var endTime = System.nanoTime()
					if (argList.isEmpty())
					{
						// Nothing to inject
						return
					}

					endTime = System.nanoTime()
					Log.d("TIME_START", ((endTime - startTime).toDouble()/1000000.0).toString())

					val argumentTypes = argList.map { arg ->
						arg.javaClass.kotlin.starProjectedType
					}

					val argTypesHash = argumentTypes.hashCode()
					val fragName = fragment.javaClass.name
					val cacheKey = "$fragName$argTypesHash"

					Log.d("TIME_0", ((endTime - startTime).toDouble()/1000000.0).toString())

					val cachedNames = FRAGMENT_CONSTRUCTOR_CACHE[cacheKey]
					if (cachedNames != null)
					{
						fragment.arguments = makeBundle(cachedNames, argList)
						return
					}

					endTime = System.nanoTime()
					Log.d("TIME_1", ((endTime - startTime).toDouble()/1000000.0).toString())
					val constructors = fragment.javaClass.kotlin.constructors

					// Loop through all the constructors for this class
					for (constructor in constructors)
					{
						endTime = System.nanoTime()
						Log.d("TIME_2", ((endTime - startTime).toDouble()/1000000.0).toString())
						val params = constructor.parameters

						if(params.isEmpty()) continue

						val constructorParamTypes = params.map { param -> param.type }
						val constructorParamTypesHash = constructorParamTypes.hashCode()

						if (constructorParamTypesHash == argTypesHash)
						{
							val bundleKeys = params.map { it.name }
							fragment.arguments = makeBundle(bundleKeys, argList)
							FRAGMENT_CONSTRUCTOR_CACHE.put(cacheKey, bundleKeys)

							return
						}
					}
					endTime = System.nanoTime()
					Log.d("TOTAL_TIME", ((endTime - startTime).toDouble()/1000000.0).toString())

					throw IllegalStateException("There was an error creating the bundle")
				}

				private fun makeBundle(bundleKeys: List<String?>, argList: Array<out Any>): Bundle
				{
					val args = Bundle()
					for (i in 0 until bundleKeys.size)
					{
						val key = bundleKeys[i]
						val value = argList[i]
						args.put(INJECT_MEMBER_PREFIX + key, value)
					}

					return args
				}
			}
		}
	}
}