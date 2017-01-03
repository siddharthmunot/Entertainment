package com.nutlabs.restaurant;

/**
 * Created by mdimran on 4/3/2016.
 */
public interface SharedConstants {
    public static final String TAG="Pinut";

    public static final String SERVER_DEFAULT_USERNAME="pinut";
    public static final String SERVER_DEFAULT_PASSWORD="welcome";
    public static final String SERVER_DEFAULT_NAME="pinut";


    public static final String SERVER_APP_PATH="/pinutuser/";
    public static final String DEFAULT_SERVER_PORT_PYTHON="8000";

    public static final int APP_ID_PINUT=1;
    public static final int APP_ID_WOOBUS=2;
    public static final int APP_ID_GENERAL=3;

    public static final String ERROR_MSG="Error_Msg";
    public static final String ERROR_MSG_WIFI_NOT_CONNECTED_PINUT="Please connect to pinut wifi hotspot and try again.";
    public static final String ERROR_MSG_WIFI_CONNECTED_ONLY_PINUT="Pinut server not found on this network. Please connect to pinut wifi hotspot and try again.";
    public static final String ERROR_MSG_WIFI_CONNECTED_DATA_CONNECTED_PINUT="Please disable the Mobile data connection and connect to pinut hotspot.";
    public static final String ERROR_MSG_SERVER_NOT_FOUND_PINUT="Pinut server not found on this network. Please try again later.";
    public static final String ERROR_MSG_DATA_CONNECTED_ONLY_PINUT="Please disable the Mobile data connection and connect to pinut hotspot.";

    public static final String API_CONNECTION_INFO="pinutconnectioninfo/";
    public static final String API_USER_DETAILS="pinutuserintro/";
    public static final String API_VIEW_DETAILS="pinutuserinfo/";
    public static final String API_FEEDBACK="pinutfeedback/";
    public static final String API_ASSESMENT="pinutuserassesment/";

    public static final String JSON_FIELD_CLIENT_MAC="cl_mac";
    public static final String JSON_FIELD_NAME="name";
    public static final String JSON_FIELD_PHONE="phone";
    public static final String JSON_FIELD_EMAIL_ID="email_id";
    public static final String JSON_FIELD_DATA="data";
    public static final String JSON_FIELD_CATEGORY="category";
    public static final String JSON_FIELD_DATA_LENGTH="data_length";
    public static final String JSON_FIELD_VIEW_LENGTH="view_length";
    public static final String JSON_FIELD_RIDE_EXPRNC="ride_experience";
    public static final String JSON_FIELD_PINUT_EXPRNC="pinut_experience";
    public static final String JSON_FIELD_USER_COMMENT="comment";
    public static final String JSON_COUNT="attempts";

    public static final String DATA_CATEGORY_MOVIE="movie";
    //public static final String DATA_CATEGORY_TV_S="email_id";
    //public static final String DATA_CATEGORY_MOVIE="email_id";

    public static final String DATA_NAME="data_name";
    public static final String DATA_CATEGORY_TYPE="data_category";

    public static final String PREF_REGISTRATION_SUCCESSFULL="register_completed";
    public static final String PREF_NAME="name";
    public static final String PREF_EMAIL="email";
    public static final String PREF_PHONE="phone";
    public static final String PREF_USER_DATA_SAVED="user_data_saved";
    public static final String PREF_SERVER_URL="Server_Url";
    public static final String PREF_SERVER_PORT="Server_Port";

    public static final String SSID_PINUT="pinut";
    public static final String SSID_WOOBUS="woobus";

    public static final String MOVIE_TAG="movietag";
    public static final String BASE_TAG_STRING="basetag";
    public static final String MOVIE_TAG_SHORTMOVIES="ShortMovie";
    public static final String DEFAULT_MOVIE_TAG="ShortMovie";
    public static final String MOVIE_TAG_ANIMATEDMOVIE="AnimatedMovie";
    public static final String MOVIE_TAG_TEDTALKS="TedTalk";
    public static final String MOVIE_TAG_OLDISGOLD="OldIsGold";
    public static final String MOVIE_TAG_VIRAL_VIDEOS="ViralVideo";
    public static final String MOVIE_TAG_NEW_ADDITION="NewAddtion";
    public static final String MOVIE_TAG_COMPUTERSKILL="computerskills";
    public static final String MOVIE_TAG_ENGLISHEDUCATION="englishandlifeskills";
    public static final String MOVIE_TAG_FARMINGANDFISHING="fishingandfarming";
    public static final String MOVIE_TAG_FINANCE="finance";
    public static final String MOVIE_TAG_HEALTH="health";
    public static final String MOVIE_TAG_ORIYA="oriyacontent";
    public static final String MOVIE_TAG_SAFETYANDSECURITY="safetyandsecurity";
    public static final String MOVIE_TAG_VOCATIONAL="learnaboutjobs";
    public static final String MOVIE_TAG_NDLM = "NDLM";

    // public static final String MOVIE_TAG_KHANACADDEMY = "";
    public static final String MOVIE_TAG_WIKIPEDIA = "";


    public static final String ACTIVITY_NAME_SHORTMOVIES="Entertainment";
    public static final String ACTIVITY_NAME_ANIMATEDMOVIE="Kids Section";
    public static final String ACTIVITY_NAME_TEDTALKS="Educational";
    public static final String ACTIVITY_NAME_OLDISGOLD="Kids Section";
    public static final String ACTIVITY_NAME_VIRAL_VIDEOS="Viral Videos";
    public static final String ACTIVITY_NAME_RECENTLYADDED="New Additions";

    public static final String PREF_SHORTMOVIES="ShortMovie";
    public static final String PREF_ANIMATEDMOVIE="AnimatedMovie";
    public static final String PREF_TEDTALKS="TedTalk";
    public static final String PREF_OLDISGOLD="OldIsGold";
    public static final String PREF_VIRALVIDEOS="ViralVideo";
    public static final String PREF_NEWADDITIONS="NewAddition";
    public static final String PREF_COMPUTERSKILLS="computerskills";
    public static final String PREF_ENGLISHEDUCATION="englishandlifeskills";
    public static final String PREF_FARMINGANDFISHING="fishingandfarming";
    public static final String PREF_FINANCE="finance";
    public static final String PREF_HEALTH="health";
    public static final String PREF_ORIYA="oriyacontent";
    public static final String PREF_SAFETYANDSECURITY="safetyandsecurity";
    public static final String PREF_VOCATIONAL="oriyacontent";






    public static final String PREF_FOLDERSTRUCTURE="FolderStruct";


    public static final String ACTIVITY_HOME = "HOME";
    public static final String ACTIVITY_COMPUTER_SKILLS = "Computer Skills";
    public static final String ACTIVITY_ENGLISH_EDUCATION = "English Education";
    public static final String ACTIVITY_VOCATIONAL_SKILLS = "Vocational Skills";
    public static final String ACTIVITY_FARMING_AND_FISHING = "Farming And Fishing";
    public static final String ACTIVITY_ORIYA_CONTENT = "Oriya Content";
    public static final String ACTIVITY_SAFETY_AND_SECURITY = "Safety And Security";
    public static final String ACTIVITY_FINANCE = "Finance";
    public static final String ACTIVITY_HEALTH = "Health";
    public static final String ACTIVITY_MCQ = "Assesments";
    public static final String ACTIVITY_NDLM_CONTENT = "NDLM";
    public static final String FOLDER_LEVEL="FolderLevel";
}