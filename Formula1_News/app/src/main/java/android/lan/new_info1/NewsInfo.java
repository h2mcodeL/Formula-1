package android.lan.new_info1;

public class NewsInfo {

    /** Variable for the section ID of the news story */
    private String mSectionId;

    /** Variable for the webTitle of the news story */
    private String mwebTitle;

    /** Variable for the Title of the news story */
    private String mUrl;

    /** Variable for the web publication date */
    private String mDatetime;

    private String mAuthor;

    /** Constructor for the structure of the News info */
    public NewsInfo (String sectionId, String webtitle, String url, String dateTime, String author){
        this.mSectionId = sectionId;
        this.mwebTitle = webtitle;
        this.mUrl = url;
        this.mDatetime = dateTime;
        this.mAuthor = author;
    }

    /** Constructor for the structure of the News info */

    /** Getter for the title - */
    public String getTitle() {
        return mSectionId;
    }

    public String getMwebTitle() {
        return mwebTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmDatetime() {
        return mDatetime;
    }

    public String getmAuthor() { return mAuthor;}
}
