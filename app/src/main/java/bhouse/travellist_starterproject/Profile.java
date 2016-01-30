package bhouse.travellist_starterproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jamais on 30/01/2016.
 */
public class Profile {
    public String firstName;
    public String lastName;
    public String login;
    public String email;
    public String pictureSrc;
    public String year;
    public String gpa;
    public String activeTime;


    public Profile (JSONObject jsonProfile) {
        try {
            firstName = jsonProfile.getString("firstname");
            lastName  = jsonProfile.getString("lastName");
            login = jsonProfile.getString("login");
            email = jsonProfile.getJSONObject("userinfo").getJSONObject("email").getString("value");
            pictureSrc = jsonProfile.getString("picture");
            year = jsonProfile.getString("promo");
            gpa = jsonProfile.getJSONArray("gpa").getJSONObject(0).getString("gpa");
            activeTime = jsonProfile.getJSONObject("nsstat").getString("active");

        } catch (JSONException e) {
            System.out.println("Profile::(JSONObject) : JSONException.");
            e.printStackTrace();
        }

    }
}
