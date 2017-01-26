package com.jakelauer.baseballtheater.GameList;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jakelauer.baseballtheater.About.AboutActivity;
import com.jakelauer.baseballtheater.Settings.SettingsActivity;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

public class DrawerItemClickListener implements ListView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectItem(position);
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
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ko-fi.com/A76217J"));
				break;
		}

		if (intent != null) {
			context.startActivity(intent);
		}
	}
}
