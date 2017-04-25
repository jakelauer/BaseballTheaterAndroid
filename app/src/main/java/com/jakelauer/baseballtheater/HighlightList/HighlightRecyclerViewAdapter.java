package com.jakelauer.baseballtheater.HighlightList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jakelauer.baseballtheater.BaseballTheater;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.R;
import com.jakelauer.baseballtheater.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.nodes.okhttputils.error.HttpErrorManager.context;

/**
 * Created by Jake on 1/24/2017.
 */

public class HighlightRecyclerViewAdapter extends RecyclerView.Adapter<HighlightRecyclerViewAdapter.ViewHolder>
{

	private final Activity mParentActivity;
	private final List<Highlight> mValues;
	private SharedPreferences mPrefs;

	public HighlightRecyclerViewAdapter(Activity parent, List<Highlight> highlights)
	{
		mValues = highlights;
		mParentActivity = parent;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(mParentActivity);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.highlight_list_content, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position)
	{
		final Boolean showVideoQualities = mPrefs.getBoolean("behavior_show_video_quality_options", true);

		Highlight highlight = mValues.get(position);

		holder.mItem = highlight;

		String title = highlight.headline;
		if (highlight.recap)
		{
			title = "Recap (score hidden)";
		}
		holder.mIdView.setText(title);
		holder.mImageView.setImageDrawable(null);

		if (highlight.thumbs != null && highlight.thumbs.thumbs != null && highlight.thumbs.thumbs.size() > 0)
		{
			int thumbIndex = this.getThumbIndex(highlight);

			String thumb = highlight.thumbs.thumbs.get(thumbIndex);

			Picasso.with(context)
					.load(thumb)
					.placeholder(R.color.colorPlaceholder)
					.into(holder.mImageView);
		}
		else
		{
			holder.mImageView.setVisibility(View.GONE);
		}

		if (BaseballTheater.isSmallDevice())
		{
			holder.mImageView.getLayoutParams().height = 250;
		}

		if (showVideoQualities)
		{
			this.setupVideoQualityLinks(highlight, holder);
		}
		else
		{
			holder.mVideoQualityOptions.setVisibility(View.GONE);
		}

		View.OnClickListener defaultListener = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				openLink(holder);
			}
		};

		holder.mIdView.setOnClickListener(defaultListener);
		holder.mImageView.setOnClickListener(defaultListener);
	}

	private int getThumbIndex(Highlight highlight)
	{
		int thumbIndex = highlight.thumbs.thumbs.size() - 4;

		String prefKey = Utility.isWifiAvailable(context)
				? "display_thumbnail_quality_wifi"
				: "display_thumbnail_quality_mobile";

		String thumbQualitySetting = mPrefs.getString(prefKey, "1");
		switch (thumbQualitySetting)
		{
			case "0":
				thumbIndex = 0;
				break;
			case "1":
				thumbIndex = highlight.thumbs.thumbs.size() - 3;
				break;
			case "2":
				thumbIndex = highlight.thumbs.thumbs.size() - 4;
				break;
		}

		return thumbIndex;
	}

	private void setupVideoQualityLinks(Highlight highlight, ViewHolder holder)
	{
		View.OnClickListener qualityListener = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				openLink((String) v.getTag());
			}
		};

		for (String url : highlight.urls)
		{
			Pattern pattern = Pattern.compile("\\d{4}K");
			Matcher matcher = pattern.matcher(url);

			String kValue = "";
			if (matcher.find())
			{
				kValue = matcher.group(0);
			}

			TextView qualityTextView = null;
			switch (kValue)
			{
				case "1200K":
					qualityTextView = holder.mVideoQuality1200K;
					break;

				case "1800K":
					qualityTextView = holder.mVideoQuality1800K;
					break;

				case "2500K":
					qualityTextView = holder.mVideoQuality2500K;
					break;
			}

			if (qualityTextView != null && kValue != "")
			{
				qualityTextView.setTag(url);
				qualityTextView.setOnClickListener(qualityListener);
			}
		}
	}

	private void openLink(String url)
	{
		OpenHighlightAsyncTask ohat = new OpenHighlightAsyncTask(mParentActivity);
		ohat.execute(url);
	}

	private void openLink(ViewHolder holder)
	{
		Integer urlIndex = this.getVideoUrlIndex(holder.mItem);
		String url = holder.mItem.urls.get(urlIndex);
		openLink(url);
	}

	private int getVideoUrlIndex(Highlight highlight)
	{
		int urlIndex = 2;

		String prefKey = Utility.isWifiAvailable(context)
				? "display_video_quality_wifi"
				: "display_video_quality_mobile";

		String qualitySetting = mPrefs.getString(prefKey, "1");
		switch (qualitySetting)
		{
			case "0":
				urlIndex = 0;
				break;

			case "1":
				urlIndex = (int) Math.floor(highlight.urls.size() / 2);
				break;

			case "2":
				urlIndex = highlight.urls.size() - 1;
				break;
		}

		return urlIndex;
	}

	@Override
	public int getItemCount()
	{
		return mValues.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		public final View mView;
		public final TextView mIdView;
		public final ImageView mImageView;
		public final TableLayout mVideoQualityOptions;
		public final TextView mVideoQuality1200K;
		public final TextView mVideoQuality1800K;
		public final TextView mVideoQuality2500K;
		public Highlight mItem;

		public ViewHolder(View view)
		{
			super(view);
			mView = view;
			mIdView = (TextView) view.findViewById(R.id.id);
			mImageView = (ImageView) view.findViewById(R.id.highlight_list_thumb);
			mVideoQualityOptions = (TableLayout) view.findViewById(R.id.video_quality_options);
			mVideoQuality1200K = (TextView) view.findViewById(R.id.video_quality_option_1200K);
			mVideoQuality1800K = (TextView) view.findViewById(R.id.video_quality_option_1800K);
			mVideoQuality2500K = (TextView) view.findViewById(R.id.video_quality_option_2500K);
		}
	}
}