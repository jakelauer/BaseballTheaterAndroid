package libs.ButterKnife

/**
 * Created by Jake on 10/24/2017.
 */

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import android.support.v4.app.DialogFragment as SupportDialogFragment
import android.support.v4.app.Fragment as SupportFragment

public fun <V : View> View.bindView(id: Int)
		: ReadWriteProperty<View, V> = required(id, viewFinder)

public fun <V : View> Activity.bindView(id: Int)
		: ReadWriteProperty<Activity, V> = required(id, viewFinder)

public fun <V : View> Dialog.bindView(id: Int)
		: ReadWriteProperty<Dialog, V> = required(id, viewFinder)

public fun <V : View> DialogFragment.bindView(id: Int)
		: ReadWriteProperty<DialogFragment, V> = required(id, viewFinder)

public fun <V : View> SupportDialogFragment.bindView(id: Int)
		: ReadWriteProperty<SupportDialogFragment, V> = required(id, viewFinder)

public fun <V : View> Fragment.bindView(id: Int)
		: ReadWriteProperty<Fragment, V> = required(id, viewFinder)

public fun <V : View> SupportFragment.bindView(id: Int)
		: ReadWriteProperty<SupportFragment, V> = required(id, viewFinder)

public fun <V : View> ViewHolder.bindView(id: Int)
		: ReadWriteProperty<ViewHolder, V> = required(id, viewFinder)

public fun <V : View> View.bindOptionalView(id: Int)
		: ReadWriteProperty<View, V?> = optional(id, viewFinder)

public fun <V : View> Activity.bindOptionalView(id: Int)
		: ReadWriteProperty<Activity, V?> = optional(id, viewFinder)

public fun <V : View> Dialog.bindOptionalView(id: Int)
		: ReadWriteProperty<Dialog, V?> = optional(id, viewFinder)

public fun <V : View> DialogFragment.bindOptionalView(id: Int)
		: ReadWriteProperty<DialogFragment, V?> = optional(id, viewFinder)

public fun <V : View> SupportDialogFragment.bindOptionalView(id: Int)
		: ReadWriteProperty<SupportDialogFragment, V?> = optional(id, viewFinder)

public fun <V : View> Fragment.bindOptionalView(id: Int)
		: ReadWriteProperty<Fragment, V?> = optional(id, viewFinder)

public fun <V : View> SupportFragment.bindOptionalView(id: Int)
		: ReadWriteProperty<SupportFragment, V?> = optional(id, viewFinder)

public fun <V : View> ViewHolder.bindOptionalView(id: Int)
		: ReadWriteProperty<ViewHolder, V?> = optional(id, viewFinder)

public fun <V : View> View.bindViews(vararg ids: Int)
		: ReadWriteProperty<View, List<V>> = required(ids, viewFinder)

public fun <V : View> Activity.bindViews(vararg ids: Int)
		: ReadWriteProperty<Activity, List<V>> = required(ids, viewFinder)

public fun <V : View> Dialog.bindViews(vararg ids: Int)
		: ReadWriteProperty<Dialog, List<V>> = required(ids, viewFinder)

public fun <V : View> DialogFragment.bindViews(vararg ids: Int)
		: ReadWriteProperty<DialogFragment, List<V>> = required(ids, viewFinder)

public fun <V : View> SupportDialogFragment.bindViews(vararg ids: Int)
		: ReadWriteProperty<SupportDialogFragment, List<V>> = required(ids, viewFinder)

public fun <V : View> Fragment.bindViews(vararg ids: Int)
		: ReadWriteProperty<Fragment, List<V>> = required(ids, viewFinder)

public fun <V : View> SupportFragment.bindViews(vararg ids: Int)
		: ReadWriteProperty<SupportFragment, List<V>> = required(ids, viewFinder)

public fun <V : View> ViewHolder.bindViews(vararg ids: Int)
		: ReadWriteProperty<ViewHolder, List<V>> = required(ids, viewFinder)

public fun <V : View> View.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<View, List<V>> = optional(ids, viewFinder)

public fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<Activity, List<V>> = optional(ids, viewFinder)

public fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<Dialog, List<V>> = optional(ids, viewFinder)

public fun <V : View> DialogFragment.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<DialogFragment, List<V>> = optional(ids, viewFinder)

public fun <V : View> SupportDialogFragment.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<SupportDialogFragment, List<V>> = optional(ids, viewFinder)

public fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<Fragment, List<V>> = optional(ids, viewFinder)

public fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<SupportFragment, List<V>> = optional(ids, viewFinder)

public fun <V : View> ViewHolder.bindOptionalViews(vararg ids: Int)
		: ReadWriteProperty<ViewHolder, List<V>> = optional(ids, viewFinder)

private val View.viewFinder: View.(Int) -> View?
	get() = { findViewById(it) }
private val Activity.viewFinder: Activity.(Int) -> View?
	get() = { findViewById(it) }
private val Dialog.viewFinder: Dialog.(Int) -> View?
	get() = { findViewById(it) }
private val DialogFragment.viewFinder: DialogFragment.(Int) -> View?
	get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }
private val SupportDialogFragment.viewFinder: SupportDialogFragment.(Int) -> View?
	get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }
private val Fragment.viewFinder: Fragment.(Int) -> View?
	get() = { view.findViewById(it) }
private val SupportFragment.viewFinder: SupportFragment.(Int) -> View?
	get() = { view!!.findViewById(it) }
private val ViewHolder.viewFinder: ViewHolder.(Int) -> View?
	get() = { itemView.findViewById(it) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
		throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?)
		= Lazy { t: T,
								  desc ->
	t.finder(id) as V? ?: viewNotFound(id, desc)
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?)
		= Lazy { t: T, desc -> t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?)
		= Lazy { t: T, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?)
		= Lazy { t: T, desc -> ids.map { t.finder(it) as V? }.filterNotNull() }

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