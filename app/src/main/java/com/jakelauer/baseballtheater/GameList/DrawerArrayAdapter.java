package com.jakelauer.baseballtheater.GameList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakelauer.baseballtheater.R;

import java.util.List;

/**
 * Created by Jake on 1/25/2017.
 */

public class DrawerArrayAdapter extends ArrayAdapter<DrawerRowItem> {

	Context context;

	public DrawerArrayAdapter(Context context, int resourceId, List<DrawerRowItem> items){
		super(context, resourceId, items);

		this.context = context;
	}

	/*private view holder class*/
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		DrawerRowItem item = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtTitle.setText(item.mTitle);
		holder.imageView.setImageResource(item.mResourceId);

		return convertView;
	}
}

