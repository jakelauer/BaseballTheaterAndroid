package com.jakelauer.baseballtheater.GameList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jakelauer.baseballtheater.About.AboutActivity;
import com.jakelauer.baseballtheater.Settings.SettingsActivity;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

public class DrawerItemClickListener implements LinearLayout.OnClickListener {
	private Context m_context;

	public DrawerItemClickListener(Context context){
		this.m_context = context;
	}

	@Override
	public void onClick(View v)
	{
		selectItem((int) v.getTag());
	}

	private void selectItem(int position) {
		Intent intent = null;
		switch (position) {
			case 0:
				intent = new Intent(context, SettingsActivity.class);
				break;

			case 1:
				intent = new Intent(context, AboutActivity.class);
				break;

			case 2:
				final Intent emailIntent = new Intent(Intent.ACTION_SEND);

				emailIntent.setData(Uri.parse("mailto:baseballtheater@gmail.com"));
				emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "baseballtheater@gmail.com" });
				emailIntent.setType("text/plain");

				intent = Intent.createChooser(emailIntent, "Choose an Email client :");
				break;

			case 3:
				String url = "https://baseball.theater/backers";
				CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
				CustomTabsIntent customTabsIntent = builder.build();
				customTabsIntent.launchUrl(m_context, Uri.parse(url));
				break;
		}

		if (intent != null) {
			this.m_context.startActivity(intent);
		}
	}
}
