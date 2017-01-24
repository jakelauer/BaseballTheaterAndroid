package com.jakelauer.baseballtheater.HighlightList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

/**
 * Created by Jake on 1/24/2017.
 */

public class HighlightRecyclerViewAdapter extends RecyclerView.Adapter<HighlightRecyclerViewAdapter.ViewHolder> {

	private final Activity mParentActivity;
	private final List<Highlight> mValues;

	public HighlightRecyclerViewAdapter(Activity parent, List<Highlight> highlights) {
		mValues = highlights;
		mParentActivity = parent;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.highlight_list_content, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		Highlight highlight = mValues.get(position);

		holder.mItem = highlight;
		holder.mIdView.setText(highlight.headline);
		holder.mImageView.setImageDrawable(null);

		if (highlight.thumbs != null && highlight.thumbs.thumbs != null && highlight.thumbs.thumbs.size() > 0) {
			String thumb = highlight.thumbs.thumbs.get(highlight.thumbs.thumbs.size() - 3);

			Picasso.with(context)
					.load(thumb)
					.placeholder(R.color.colorPlaceholder)
					.into(holder.mImageView);
		}
		else{
			holder.mImageView.setVisibility(View.GONE);
		}

		if(BaseballTheater.isSmallDevice()) {
			holder.mImageView.getLayoutParams().height = 250;
		}

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = holder.mItem.urls.get(0);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				mParentActivity.startActivity(browserIntent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mIdView;
		public final ImageView mImageView;
		public Highlight mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mIdView = (TextView) view.findViewById(R.id.id);
			mImageView = (ImageView) view.findViewById(R.id.highlight_list_thumb);
		}
	}
}