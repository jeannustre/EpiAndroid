package bhouse.travellist_starterproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {

  private RecyclerView mRecyclerView;
  private StaggeredGridLayoutManager mStaggeredLayoutManager;
  private Menu menu;
  private boolean isListView;
//  private EpiListAdapter mAdapter;

  /**/
  public  API                   api = new API();
  private AutoCompleteTextView  userLogin;
  private EditText              userPwd;
  /**/


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_layout);

    userLogin = (AutoCompleteTextView) findViewById(R.id.loginTextField);
    userPwd = (EditText) findViewById(R.id.passwordTextField);


//    isListView = true;
//    mRecyclerView = (RecyclerView) findViewById(R.id.list);
//    mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//    mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
//
//    mAdapter = new EpiListAdapter(this);
//    mRecyclerView.setAdapter(mAdapter);
//    mAdapter.setOnItemClickListener(onItemClickListener);
  }

//  EpiListAdapter.OnItemClickListener onItemClickListener = new EpiListAdapter.OnItemClickListener() {
//    @Override
//    public void onItemClick(View v, int position) {
//      Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
//      Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//      intent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);
//      startActivity(intent);
//    }
//
//
//  };

  private void setUpActionBar() {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    this.menu = menu;
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_toggle) {
      toggle();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void toggle() {
    MenuItem item = menu.findItem(R.id.action_toggle);
    if (isListView) {
      item.setIcon(R.drawable.ic_action_list);
      item.setTitle("Show as list");
      isListView = false;
    } else {
      item.setIcon(R.drawable.ic_action_grid);
      item.setTitle("Show as grid");
      isListView = true;
    }
  }


    public void     switchToFullView(View v)
    {
        Intent intentMyAccount = new Intent(getApplicationContext(), UserActivity.class);

        intentMyAccount.putExtra("userLogin", userLogin.getText().toString());
        intentMyAccount.putExtra("session_token", api.session_token);
        startActivity(intentMyAccount);
    }

  /* called by connectButton.onClick() */
  public boolean  connection(View _connectionView) {

    if (!api.connect(userLogin.getText().toString(), userPwd.getText().toString()))
      return false;

    System.out.println("Token = " + api.session_token);
    api.retrieveProfileInformation(userLogin.getText().toString(), profileInformationRequest());
    setContentView(R.layout.profil_layout);
    return true;
  }

  /* Used as debug for profil_layout refresh button */
  public void   refreshProfileInformation(View _profileView) {
    api.retrieveProfileInformation(((EditText)(findViewById(R.id.loginSearchTextField))).getText().toString(), profileInformationRequest());
  }



  /* Fill the login_layout with __user__ information */
  public JsonHttpResponseHandler   profileInformationRequest() {
    JsonHttpResponseHandler r = new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        super.onStart();
        System.out.println("profileInformationRequest::onStart()");

      }

      @Override
      public void onFinish() {
        super.onFinish();
        System.out.println("profileInformationRequest::onFinish()");
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

          System.out.println("profileInformationRequest::onSuccess()");
          Profile profile = new Profile(response);

          System.out.println("profile.login = " + profile.login);
          System.out.println("profile.firstName = " + profile.firstName);
          System.out.println("profile.year = " + profile.year);
          System.out.println("profile.gpa = " + profile.gpa);
          System.out.println("profile.activeTime = " + profile.activeTime);
          ((TextView) findViewById(R.id.loginTextView)).setText(profile.login);
          ((TextView) findViewById(R.id.nameTextView)).setText(profile.firstName);
          ((TextView) findViewById(R.id.yearTextView)).setText(profile.year);
          ((TextView) findViewById(R.id.gpaTextView)).setText(profile.gpa);
          ((TextView) findViewById(R.id.logTimeTextView)).setText(profile.activeTime);
          Picasso.with(getApplicationContext()).load(profile.pictureSrc).into((ImageView)findViewById(R.id.profilPicture));

//        try {
//            String logTime = response.getJSONObject("nsstat").getString("active") + " h";
//            ((TextView) findViewById(R.id.loginTextView)).setText(response.getString("login"));
//            ((TextView) findViewById(R.id.nameTextView)).setText(response.getString("title"));
//            ((TextView) findViewById(R.id.yearTextView)).setText(response.getString("promo"));
//            ((TextView) findViewById(R.id.gpaTextView)).setText(response.getJSONArray("gpa").getJSONObject(0).getString("gpa"));
//            ((TextView) findViewById(R.id.logTimeTextView)).setText(logTime);
//            Picasso.with(getApplicationContext()).load(response.getString("picture")).into((ImageView)findViewById(R.id.profilPicture));
//        }
//        catch (JSONException e) { e.printStackTrace(); }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        System.out.println("____profileInformationRequest() : Failed to retrieve user information");
      }
    };

    return r;
  }


}
