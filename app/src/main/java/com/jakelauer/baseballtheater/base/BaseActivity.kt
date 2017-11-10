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
import com.google.android.gms.cast.framework.CastSession
import com.jakelauer.baseballtheater.R
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_base.*
import com.google.android.gms.cast.framework.SessionManager




/**
 * Created by Jake on 10/20/2017.
 */

abstract class BaseActivity(val m_canCast: Boolean) : AppCompatActivity()
{
	@get:LayoutRes
	protected abstract val m_layoutResId: Int


	private var m_mediaRouteMenuItem: MenuItem? = null

	private lateinit var m_castContext: CastContext
	private lateinit var m_mediaRouter: MediaRouter
	private lateinit var m_mediaRouteSelector: MediaRouteSelector
	private lateinit var m_mediaRouterCallback: MediaRouter.Callback
	private lateinit var m_castSessionManager: SessionManager
	var m_castSession: CastSession? = null
	var m_castDevice: CastDevice? = null

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		Icepick.restoreInstanceState(this, savedInstanceState)

		setContentView(m_layoutResId)

		if(m_canCast)
		{
			CastButtonFactory.setUpMediaRouteButton(applicationContext, media_route_button)

			m_castContext = CastContext.getSharedInstance(this)

			m_mediaRouter = MediaRouter.getInstance(this)
			m_mediaRouteSelector = MediaRouteSelector.Builder()
					.addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
					.addControlCategory(CastMediaControlIntent.categoryForCast(getString(R.string.app_id)))
					.build()

			m_mediaRouterCallback = object : MediaRouter.Callback()
			{
				override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo)
				{
					super.onRouteSelected(router, route)
					m_castDevice = CastDevice.getFromBundle(route.extras)
				}

				override fun onRouteUnselected(router: MediaRouter?, route: MediaRouter.RouteInfo?)
				{
					super.onRouteUnselected(router, route)
					m_castDevice = null
				}
			}

			m_castSessionManager = CastContext.getSharedInstance(this).sessionManager
			m_castSession = m_castSessionManager.currentCastSession
		}

		onBindView()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		super.onCreateOptionsMenu(menu)

		if(m_canCast)
		{
			menuInflater.inflate(R.menu.cast, menu)
			m_mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
			val provider = MenuItemCompat.getActionProvider(m_mediaRouteMenuItem) as MediaRouteActionProvider
			provider.routeSelector = m_mediaRouteSelector
		}

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

		if (m_canCast)
		{
			m_castSession = m_castSessionManager.currentCastSession
			m_mediaRouter.addCallback(m_mediaRouteSelector, m_mediaRouterCallback, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN)
		}
	}

	override fun onPause()
	{
		super.onPause()
		if (isFinishing)
		{
			m_mediaRouter.removeCallback(m_mediaRouterCallback)
		}
		m_castSession = null
	}

	public fun setPref(key: String, value: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.putString(key, value)
		editor.apply()
	}

	public fun getPref(key: String): String
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		return sharedPref.getString(key, "")
	}

	public fun clearPref(key: String)
	{
		val sharedPref = getPreferences(Context.MODE_PRIVATE)
		val editor = sharedPref.edit()
		editor.remove(key)
		editor.apply()
	}
}
