package android.lan.new_info1;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


import java.util.List;

public class NewsInfoLoader extends AsyncTaskLoader<List<NewsInfo>> {

    private String mURL;

    public NewsInfoLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<NewsInfo> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<NewsInfo> newsInfoList = QueryUtils.fetchNewsData(mURL);
        return newsInfoList;
    }
}
