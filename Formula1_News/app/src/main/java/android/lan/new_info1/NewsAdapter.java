package android.lan.new_info1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<NewsInfo> {

    //Separator for datetime format
    private static final String DATE_SEPARATOR = "T";

    public NewsAdapter(Context context, ArrayList<NewsInfo> newsitems) {
        super(context, 0, newsitems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if the existing view is being reused.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,
                    parent, false);
        }

        // Get the object in the current position
        NewsInfo currentnews = getItem(position);

        //holds the amended settings for the DATETIME
          String primaryDate;

        String time1 = currentnews.getmDatetime();
        if (time1.contains(DATE_SEPARATOR)) {
            String[] separatedItems = time1.split(DATE_SEPARATOR);
            primaryDate = separatedItems[0];

        } else {

            primaryDate = "No date available!";
        }

        //Textview layout for the Custom Array information page
        //Get the main article text and display
            TextView mainArticle = convertView.findViewById(R.id.article);
            mainArticle.setText(currentnews.getMwebTitle());

            //Get the section ID name and display in the view
            TextView sectionId = convertView.findViewById(R.id.sectionId);
            sectionId.setText(currentnews.getTitle());

            //Get the dateview, parse and display the date
            TextView dateView = convertView.findViewById(R.id.dateview);
            dateView.setText("Date Published: " + primaryDate);

            //Get the authors name and display
            TextView tvAuthor = convertView.findViewById(R.id.author);
            tvAuthor.setText(currentnews.getmAuthor());

            return convertView;
        }
    }
