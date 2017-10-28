package com.jakelauer.baseballtheater.utils

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
/**
 * Created by Jake on 10/25/2017.
 */

@Suppress("UNCHECKED_CAST")
class Inject<T>
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
}