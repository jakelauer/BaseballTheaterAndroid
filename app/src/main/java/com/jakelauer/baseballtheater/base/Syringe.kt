package com.jakelauer.baseballtheater.base

import android.os.Bundle
import android.support.v4.app.Fragment

class Syringe<T : Any> : kotlin.properties.ReadWriteProperty<Fragment, T>
{
	var value: T? = null

	@Suppress("UNCHECKED_CAST")
	override operator fun getValue(thisRef: android.support.v4.app.Fragment, property: kotlin.reflect.KProperty<*>): T
	{
		if(value == null)
		{
			val args = thisRef.arguments ?: throw IllegalStateException("No args are set")
			value = args.get(property.name) as T
		}
		return value ?: throw IllegalStateException("Value doesn't exist in properties")
	}

	override operator fun setValue(thisRef: android.support.v4.app.Fragment, property: kotlin.reflect.KProperty<*>, value: T)
	{
		if(thisRef.arguments == null) thisRef.arguments = android.os.Bundle()

		val args = thisRef.arguments
		args?.let {
			val key = property.name

			when(value)
			{
				is Bundle                -> it.putBundle(key, value)
				is Byte                  -> it.putByte(key, value)
				is ByteArray             -> it.putByteArray(key, value)
				is Char                  -> it.putChar(key, value)
				is CharArray             -> it.putCharArray(key, value)
				is CharSequence          -> it.putCharSequence(key, value)
				is Float                 -> it.putFloat(key, value)
				is Int                   -> it.putInt(key, value)
				is Long                  -> it.putLong(key, value)
				is String                -> it.putString(key, value)
				is Short                 -> it.putShort(key, value)
				is android.os.Parcelable -> it.putParcelable(key, value)
				is java.io.Serializable  -> it.putSerializable(key, value)
				else                     -> throw IllegalStateException(
						"Type ${value.javaClass.name} type for variable ${property.name} can't be used as an argument")
			}
		}
	}
}

class NullableArgument<T> : kotlin.properties.ReadWriteProperty<Fragment, T?>
{
	var value: T? = null

	@Suppress("UNCHECKED_CAST")
	override operator fun getValue(thisRef: android.support.v4.app.Fragment, property: kotlin.reflect.KProperty<*>): T?
	{
		// If the fragment does not have any arguments, we assume that no properties were provided
		if(thisRef.arguments == null)
		{
			return null
		}

		if(value == null)
		{
			val args = thisRef.arguments ?: throw IllegalStateException("No args are set")
			value = args.get(property.name) as T
		}

		return value ?: throw IllegalStateException("Value doesn't exist in properties")
	}

	override operator fun setValue(thisRef: android.support.v4.app.Fragment, property: kotlin.reflect.KProperty<*>, value: T?)
	{
		if(value == null)
		{
			return
		}

		if(thisRef.arguments == null) {
			thisRef.arguments = android.os.Bundle()
		}

		val args = thisRef.arguments
		args?.let {
			val key = property.name

			when(value)
			{
				is Bundle                -> it.putBundle(key, value)
				is Byte                  -> it.putByte(key, value)
				is ByteArray             -> it.putByteArray(key, value)
				is Char                  -> it.putChar(key, value)
				is CharArray             -> it.putCharArray(key, value)
				is CharSequence          -> it.putCharSequence(key, value)
				is Float                 -> it.putFloat(key, value)
				is Int                   -> it.putInt(key, value)
				is Long                  -> it.putLong(key, value)
				is String                -> it.putString(key, value)
				is Short                 -> it.putShort(key, value)
				is android.os.Parcelable -> it.putParcelable(key, value)
				is java.io.Serializable  -> it.putSerializable(key, value)
				is Any                   -> throw IllegalStateException(
						"Type ${value.javaClass.name} type for variable ${property.name} can't be used as an argument")
				else                     -> throw IllegalStateException(
						"Property ${property.name} got busted somehow")
			}
		}
	}
}