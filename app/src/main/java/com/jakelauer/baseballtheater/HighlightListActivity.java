package com.jakelauer.baseballtheater;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.GameSummary;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.Highlight;
import com.jakelauer.baseballtheater.MlbDataServer.DataStructures.HighlightsCollection;
import com.jakelauer.baseballtheater.MlbDataServer.GameDetailCreator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HighlightListActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HighlightListActivity extends AppCompatActivity {

	public Date date = new Date();

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			String dateString = year + "/" + (month + 1) + "/" + day;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			try {
				HighlightListActivity gameListActivity = (HighlightListActivity) getActivity();
				gameListActivity.date = formatter.parse(dateString);
				gameListActivity.setupRecyclerView((RecyclerView) gameListActivity.findViewById(R.id.game_list));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.game_list);
        assert recyclerView != null;
        try {
            setupRecyclerView((RecyclerView) recyclerView);
        }
        catch(IOException e){
            e.printStackTrace();
        }

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

        if (findViewById(R.id.game_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) throws IOException {
		GameSummary gameSummary = (GameSummary) getIntent().getSerializableExtra(HighlightListFragment.ARG_GAME_SUMMARY);

		GameDetailCreator detailCreator = new GameDetailCreator(gameSummary.gameDataDirectory, false);
		HighlightsCollection highlightsCollection = detailCreator.getHighlights();

		if(highlightsCollection != null && highlightsCollection.highlights != null) {
			recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(highlightsCollection.highlights));
		}
/*
        GameSummaryCreator gsCreator = new GameSummaryCreator();
        GameSummaryCollection gsCollection = null;
        try {
            //String dateString = "2016/06/16";
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			//Date date = formatter.parse(dateString);
			Date date = this.date;

            gsCollection = gsCreator.GetSummaryCollection(date);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(gsCollection != null && gsCollection.GameSummaries != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(gsCollection.GameSummaries));
        }*/
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Highlight> mValues;

        public SimpleItemRecyclerViewAdapter(List<Highlight> highlights) {
            mValues = highlights;
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

			if(highlight.thumbs != null && highlight.thumbs.thumbs != null && highlight.thumbs.thumbs.size() > 0) {
				String thumb = highlight.thumbs.thumbs.get(highlight.thumbs.thumbs.size() - 1);
				downloadImage(thumb, holder.mImageView);
			}

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					String url = holder.mItem.urls.get(0);
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(browserIntent);
                }
            });
        }

		private void downloadImage(String url, ImageView mImageView){
			new DownloadImage(mImageView).execute(url);
		}

		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		public void setImage(Drawable drawable, ImageView mImageView) {
			if (mImageView != null) {
				mImageView.setImageDrawable(drawable);
			}
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

		public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

			private ImageView mImageView;

			public DownloadImage(ImageView mImageView){
				super();
				this.mImageView = mImageView;
			}

			@Override
			protected Drawable doInBackground(String... arg0) {
				// This is done in a background thread
				return downloadImage(arg0[0]);
			}

			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			protected void onPostExecute(Drawable image)
			{
				setImage(image, this.mImageView);
			}

			private Drawable downloadImage(String _url)
			{
				//Prepare to download image
				URL url;
				BufferedOutputStream out;
				InputStream in;
				BufferedInputStream buf;

				//BufferedInputStream buf;
				try {
					url = new URL(_url);
					in = url.openStream();

					// Read the inputstream
					buf = new BufferedInputStream(in);

					// Convert the BufferedInputStream to a Bitmap
					Bitmap bMap = BitmapFactory.decodeStream(buf);
					if (in != null) {
						in.close();
					}
					if (buf != null) {
						buf.close();
					}

					return new BitmapDrawable(bMap);

				} catch (Exception e) {
					Log.e("Error reading file", e.toString());
				}

				return null;
			}

		}
    }

}