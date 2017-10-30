package com.jakelauer.baseballtheater.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.MediaRouteActionProvider
import android.support.v7.media.MediaControlIntent
import android.support.v7.media.MediaRouteSelector
import android.support.v7.media.MediaRouter
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.jakelauer.baseballtheater.R
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_base.*


/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseActivity : AppCompatActivity()
{
	@get:LayoutRes
	protected abstract val layoutResId: Int

	lateinit var m_castContext: CastContext

	private var m_mediaRouteMenuItem: MenuItem? = null

	lateinit var mediaRouter: MediaRouter
	lateinit var mediaRouteSelector: MediaRouteSelector
	lateinit var mMediaRouterCallback: MediaRouter.Callback
	var mCastDevice: CastDevice? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		Icepick.restoreInstanceState(this, savedInstanceState)

		setContentView(layoutResId)

		CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)

		m_castContext = CastContext.getSharedInstance(this)

		mediaRouter = MediaRouter.getInstance(this)
		mediaRouteSelector = MediaRouteSelector.Builder()
				.addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
				.addControlCategory(CastMediaControlIntent.categoryForCast(getString(R.string.app_id)))
				.build()

		mMediaRouterCallback = object : MediaRouter.Callback()
		{
			override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo)
			{
				super.onRouteSelected(router, route)
				mCastDevice = CastDevice.getFromBundle(route.extras)
			}

			override fun onRouteUnselected(router: MediaRouter?, route: MediaRouter.RouteInfo?)
			{
				super.onRouteUnselected(router, route)
				mCastDevice = null
			}
		}

		onBindView()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		super.onCreateOptionsMenu(menu)
		menuInflater.inflate(R.menu.cast, menu)
		m_mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
		val provider = MenuItemCompat.getActionProvider(m_mediaRouteMenuItem) as MediaRouteActionProvider
		provider.routeSelector = mediaRouteSelector
		return true
	}

	protected abstract fun onBindView()

	protected fun setMainFragment(fragment: Fragment)
	{
		val fragmentManager = supportFragmentManager
		val fragmentTransaction = fragmentManager?.beginTransaction()
		fragmentTransaction?.replace(R.id.content_frame, fragment)
		fragmentTransaction?.commit()
	}

	override fun onResume()
	{
		super.onResume()
		mediaRouter.addCallback(mediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN)
	}

	override fun onPause()
	{
		super.onPause()
		if (isFinishing)
		{
			mediaRouter.removeCallback(mMediaRouterCallback)
		}
	}

	fun setPref(key: String, value: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.putString(key, value)
		editor.commit()
	}

	fun getPref(key: String): String
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		return sharedPref.getString(key, "")
	}

	fun clearPref(key: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.remove(key)
	}
}
