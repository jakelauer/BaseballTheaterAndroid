package com.jakelauer.baseballtheater.utils

import android.content.Context
import android.support.annotation.ColorRes
import com.jakelauer.baseballtheater.R
import java.net.ContentHandler

/**
 * Created by Jake on 10/25/2017.
 */
class TeamColors
{
	companion object
	{
		fun getTeamColor(teamName: String, context: Context): Int
		{
			val colorRes = getTeamColorResId(teamName)
			return context.resources.getColor(colorRes)
		}

		@ColorRes
		fun getTeamColorResId(teamName: String): Int
		{
			var color = R.color.colorAccent

			when (teamName)
			{
				"ana" -> color = R.color.team_color_ana
				"az" -> color = R.color.team_color_ari
				"ari" -> color = R.color.team_color_ari
				"atl" -> color = R.color.team_color_atl
				"bal" -> color = R.color.team_color_bal
				"bos" -> color = R.color.team_color_bos
				"chc" -> color = R.color.team_color_chc
				"chw" -> color = R.color.team_color_chw
				"cws" -> color = R.color.team_color_chw
				"cin" -> color = R.color.team_color_cin
				"cle" -> color = R.color.team_color_cle
				"col" -> color = R.color.team_color_col
				"det" -> color = R.color.team_color_det
				"mia" -> color = R.color.team_color_mia
				"hou" -> color = R.color.team_color_hou
				"kc" -> color = R.color.team_color_kc
				"la" -> color = R.color.team_color_la
				"mil" -> color = R.color.team_color_mil
				"min" -> color = R.color.team_color_min
				"nym" -> color = R.color.team_color_nym
				"nyy" -> color = R.color.team_color_nyy
				"oak" -> color = R.color.team_color_oak
				"phi" -> color = R.color.team_color_phi
				"pit" -> color = R.color.team_color_pit
				"stl" -> color = R.color.team_color_stl
				"sdp" -> color = R.color.team_color_sdp
				"sf" -> color = R.color.team_color_sf
				"sea" -> color = R.color.team_color_sea
				"tb" -> color = R.color.team_color_tb
				"tex" -> color = R.color.team_color_tex
				"tor" -> color = R.color.team_color_tor
				"was" -> color = R.color.team_color_was
			}

			return color
		}
	}
}