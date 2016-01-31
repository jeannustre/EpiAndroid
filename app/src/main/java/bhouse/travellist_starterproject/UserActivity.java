package bhouse.travellist_starterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jamais on 30/01/2016.
 */
public class    UserActivity extends FragmentActivity {

    private     API                  api = new API();
    private     String               userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent  init = getIntent();

//        setContentView(R.layout.fragment_user_info_fragment);

        System.out.println("--------------> UserActivity :: oncreate() with intent for user : " + init.getStringExtra("userLogin"));
        System.out.println("--------------> UserActivity :: oncreate() with intent for user : " + init.getStringExtra("session_token"));
        api.session_token = init.getStringExtra("session_token");
        userLogin = init.getStringExtra("userLogin");

        if (userLogin.equals("null") || api.session_token.equals("null"));

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.profil_layout, container, false);
        return mainView;
    }
}
