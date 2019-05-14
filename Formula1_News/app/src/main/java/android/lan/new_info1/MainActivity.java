package android.lan.new_info1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsInfo>> {

    /**This tag is used for the log messages*/
    private static final String LOG_TAG = MainActivity.class.getName();

    /** URL to query the Guardian API dataset. Additional query items in URI Builder list below  */
    private static final String url_response = "https://content.guardianapis.com/search?section?";

    private static final int NEWAPP_LOADER_ID = 1; /**ID for the data loader */

    /** NewsAdapter object */
    private NewsAdapter newsAdapter;

    /** TextView that is displayed when the list is empty */
     private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The list view for display on the main screen
        ListView newsInfoView = findViewById(R.id.listview);

        //set empty view when no data is found, this takes the place of the listView
        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsInfoView.setEmptyView(mEmptyStateTextView);

        //create a new adapter that takes an empty list of news items
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsInfo>());

        /** Log to provide data if the adapter fails */
        Log.v(LOG_TAG, newsAdapter + "news output");

        newsInfoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //find the current newsitem that was clicked on
                NewsInfo currentNewItem = newsAdapter.getItem(position);

                String news_url = currentNewItem.getmUrl();

                /**Log to provide data if the onClickListener cannot show the clicked item location */
                Log.v("The position URL", news_url);

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(news_url));

                startActivity(websiteIntent);
            }
        });

        //set the adapter on the list
        newsInfoView.setAdapter(newsAdapter);

        //Check the state of the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the current active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch the data.
        if (networkInfo != null && networkInfo.isConnected()) {

            //Get a reference to the loaderManager, to interact with the LoaderManager
            LoaderManager loaderManager = getSupportLoaderManager();

            //Initialize the loader then pass in the ID.
            loaderManager.initLoader(NEWAPP_LOADER_ID, null, this);
        } else {
            //display error, hide loading indicator to display error msg. This is for the processing screen
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);

            //Update the view
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @NonNull
    @Override
    public Loader<List<NewsInfo>> onCreateLoader(int i, @Nullable Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //this creates the OrderBy view in the shared Preference pane
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_orderby_key),
                getString(R.string.settings_orderby_default));

        String minDate = sharedPrefs.getString(
                getString(R.string.settings_earliest_date_key),         //settings_key_min_date),
                getString(R.string.settings_earliest_date_default));     //settings_default_minimum_date));

        String maxDate = sharedPrefs.getString(
                getString(R.string.settings_max_date_key),
                getString(R.string.settings_max_date_default));

        Uri baseUri = Uri.parse(url_response);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Build up the uri query by attaching the items below to the query above in the variable - url_response
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("from-date", minDate);
        uriBuilder.appendQueryParameter("to-date",  maxDate);
        uriBuilder.appendQueryParameter("useDate", "published");
        uriBuilder.appendQueryParameter("q", "\"Formula One\"");
        uriBuilder.appendQueryParameter("api-key", "test");

        Log.v("MainActivity",uriBuilder + ": the results!");

        return new NewsInfoLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsInfo>> loader, List<NewsInfo> newsInfos) {

        //Show loading indicator while data is loading. Hide indicator once data has loaded
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        //set empty state text to display "No Formula 1 new found
        mEmptyStateTextView.setText(R.string.no_news);

        newsAdapter.clear();

        if (newsInfos != null && !newsInfos.isEmpty()) {
            newsAdapter.addAll(newsInfos);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsInfo>> loader) {
        // Loader reset, to clear out existing data.
        newsAdapter.clear();
    }

    //Options menu for the settings and about items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Options menu items for selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            int itemId = item.getItemId();

            switch(itemId) {
                case R.id.action_settings:
                    Intent settingsIntent = new Intent(this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;
                case R.id.other_settings:
                    Intent aboutPage = new Intent(this, AboutPage.class);
                    startActivity(aboutPage);
                    break;
                default:
                    break;

        }
        return super.onOptionsItemSelected(item);
    }
}