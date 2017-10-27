package com.jakelauer.baseballtheater.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Jake on 10/25/2017.
 */

fun <Prop : Any> Fragment.inject(arg: String)
		: ReadWriteProperty<Fragment, Prop> = getArg(arg, this)

fun <Prop : Any> Activity.inject(arg: String)
		: ReadWriteProperty<Activity, Prop> = getArg(arg, this)


@Suppress("UNCHECKED_CAST")
private fun <T, Prop : Any> getArg(arg: String, frag: Fragment)
		= Lazy { t: T, desc -> findArgumentInFragment<Prop>(arg, frag) }

@Suppress("UNCHECKED_CAST")
private fun <T, Prop : Any> getArg(arg: String, frag: Activity)
		= Lazy { t: T, desc -> findArgumentInActivity<Prop>(arg, frag) }

@Suppress("UNCHECKED_CAST")
private fun <Prop : Any> findArgumentInFragment(arg: String, frag: Fragment): Prop
{
	val value: Prop
	try
	{
		value = frag.arguments.get(arg) as Prop
	}
	catch (e: Throwable)
	{
		throw Exception("Injector: Cannot cast argument to type")
	}

	return value
}

@Suppress("UNCHECKED_CAST")
private fun <Prop : Any> findArgumentInActivity(arg: String, act: Activity): Prop
{
	val value: Prop
	try
	{
		value = act.intent.extras.get(arg) as Prop
	}
	catch (e: Throwable)
	{
		throw Exception("Injector: Cannot cast argument to type")
	}

	return value
}

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadWriteProperty<T, V>
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