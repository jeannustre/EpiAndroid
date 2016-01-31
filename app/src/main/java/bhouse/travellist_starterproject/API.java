package bhouse.travellist_starterproject;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import com.google.gson.Gson;

/**
 * Created by Jamais on 29/01/2016.
 */

public class API {
    private final static String     API_addr = "https://epitech-api.herokuapp.com/";
    private static AsyncHttpClient  client = new AsyncHttpClient();
    public  String                  session_token = "null";

    /*
    * Send a connection request and retrieves the session's token
    * return false on connection failure. */
    public boolean      connect(String login, String password) {
        RequestParams   p = new RequestParams();

        p.put("login", login);
        p.put("password", password);
        JsonHttpResponseHandler r = connectionRequest();

        client.post(API_addr + "login", p, r);
        return !session_token.equals("null");

    }

    /*
    * Get __user information to fill a profile_layout view
    * Used by MainActivity::connection() to get user's information
    * Used by MainActivity::refreshProfileInformation()*/
    public void retrieveProfileInformation(String user, JsonHttpResponseHandler callBack) {
        RequestParams   p = new RequestParams();

        p.put("token", session_token);
        p.put("user", user);
        System.out.println("API::retrieveProfileInformation(" + "token = " + session_token + ", user = " + user + ")");
        client.get(API_addr + "user", p, callBack);
    }

    public void retrieveUserMark(String user, JsonHttpResponseHandler callBack) {
        RequestParams p = new RequestParams();

        p.put("token", session_token);
        client.get(API_addr + "marks", p, callBack);
    }

/*
* pre-made requests called by API methods
********/

    /* Retrieve and set the session's token. if failed, api.session_token = "null"*/
    private JsonHttpResponseHandler     connectionRequest() {
        JsonHttpResponseHandler         r = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    session_token = response.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                session_token = "null";
                System.out.println("Connection Failure");
            }
        };
        return r;
    }
}
