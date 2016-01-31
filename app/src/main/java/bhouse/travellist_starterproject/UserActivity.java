package bhouse.travellist_starterproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jamais on 30/01/2016.
 */
public class    UserActivity extends FragmentActivity {

    private     API                         api = new API();
    private     String                      userLogin;
    private     final ArrayList<MarkListItem>   markList = new ArrayList<>();
    private     Profile                     _profile;

    private class MarkListItem {
        public String mProjectName;
        public String mProjectMark;
        public String mProjectRater;

        public MarkListItem(String projectName, String projectMark, String projectRater)
        {
            super();
            mProjectName = projectName;
            mProjectMark = projectMark;
            mProjectRater = projectRater;
        }
    }

    private class       MarksAdapter extends ArrayAdapter<MarkListItem> {
        Context         _context;
        int             _layoutRessourceId;
        ArrayList<MarkListItem>  _content;

        private class MarkItemHolder {
            TextView    name;
            TextView    mark;
            TextView    rater;
        }


        public MarksAdapter(Context context, int layoutId, ArrayList<MarkListItem> content)
        {
            super(context, 0, content);

            _context = context;
            _layoutRessourceId = layoutId;
            _content = content;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View field = convertView;
            MarkItemHolder  holder = null;

            if (field == null)
            {
                LayoutInflater infl = ((Activity)_context).getLayoutInflater();
                field = infl.inflate(_layoutRessourceId, parent, false);
                holder = new MarkItemHolder();
                holder.name = (TextView)field.findViewById(R.id.projectName);
                holder.mark = (TextView)field.findViewById(R.id.projectMark);
                holder.rater = (TextView)field.findViewById(R.id.projectRater);
                field.setTag(holder);
            }
            else
            {
                holder = (MarkItemHolder)field.getTag();
            }

            MarkListItem item = _content.get(position);
            holder.name.setText(item.mProjectName);
            holder.mark.setText(item.mProjectMark);
            holder.rater.setText(item.mProjectRater);

            return field;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent  init = getIntent();
        api.session_token = init.getStringExtra("session_token");
        userLogin = init.getStringExtra("userLogin");

        setContentView(R.layout.fragment_user_info_fragment);


        api.retrieveProfileInformation(userLogin, retrieveUserInfos());
        api.retrieveUserMark(userLogin, markCallBack());

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.profil_layout, container, false);
        return mainView;
    }

    public JsonHttpResponseHandler retrieveUserInfos() {
        JsonHttpResponseHandler r = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                _profile = new Profile(response);

                String fullName = _profile.firstName + " " + _profile.lastName;
                ((TextView) findViewById(R.id.entry_loginTextView)).setText(_profile.login);
                ((TextView) findViewById(R.id.fullNameTextView)).setText(fullName);
                ((TextView) findViewById(R.id.entry_yearTextView)).setText(_profile.year);
                ((TextView) findViewById(R.id.entry_gpaTextView)).setText(_profile.gpa);
                ((TextView) findViewById(R.id.emailTextView)).setText(_profile.email);
                Picasso.with(getApplicationContext()).load(_profile.pictureSrc).into((ImageView) findViewById(R.id.profile_picture));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
        return r;
    }

    public JsonHttpResponseHandler markCallBack() {
        JsonHttpResponseHandler    callBack = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray marks = response.getJSONArray("notes");

                    int start = marks.length() <= 5 ? 0 : marks.length() - 5;
                    int position = 0;
                    for (int i = start; i < marks.length(); i++)
                    {
                        try {
                                JSONObject m = marks.getJSONObject(i);
                                markList.add(position, new MarkListItem(m.getString("title"), m.getString("final_note"), m.getString("correcteur")));
                            } catch (JSONException e) {e.printStackTrace();}
                    }
                } catch(JSONException e) {e.printStackTrace(); }

                MarksAdapter mAdapter = new MarksAdapter(UserActivity.this, R.layout.marklistitem, markList);
                ((ListView) findViewById(R.id.lastMarksListView)).setAdapter(mAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };

        return callBack;
    }
}
