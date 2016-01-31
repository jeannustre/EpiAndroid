package bhouse.travellist_starterproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jamais on 30/01/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Profile {
    public String firstName;
    public String lastName;
    public String login;
    public String email;
    public String pictureSrc;
    public String year;
    public String gpa;
    public String activeTime;

    public Profile  () {
        super();
    }
    public Profile (JSONObject jsonProfile) {
        super();

        try {
            firstName = jsonProfile.getString("firstname");
            lastName  = jsonProfile.getString("lastname");
            login = jsonProfile.getString("login");
            pictureSrc = jsonProfile.getString("picture");
            email = jsonProfile.getString("internal_email");
            year = jsonProfile.getString("promo");
            gpa = jsonProfile.getJSONArray("gpa").getJSONObject(0).getString("gpa");
            activeTime = jsonProfile.getJSONObject("nsstat").getString("active");

        } catch (JSONException e) {
            System.out.println("Profile::(JSONObject) : JSONException.");
            e.printStackTrace();
        }

    }
}
