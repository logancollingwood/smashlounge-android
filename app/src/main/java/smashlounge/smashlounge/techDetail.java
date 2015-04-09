package smashlounge.smashlounge;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class techDetail extends ActionBarActivity {
    private static final String TAG = "techDetail";
    // Caller.
    private ServerCall caller;
    private JSONParser jsonParser;

    private static String techsURL = "http://dev.smashlounge.com/api/techniques?tech=";

    //10.0.2.2 is the address used by the Android emulators to refer to the host address
    // change this to the IP of another host if required

    private static String jsonResult = "success";
    private static String jsonKey = "tech";
    JSONArray resultTechArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_detail);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //capture the parameter we sent, namely the technique name
        Bundle b = getIntent().getExtras();
        String techName = b.getString("key");

        TextView techTitle = (TextView) findViewById(R.id.techTitle);
        techTitle.setText(techName);

        jsonParser = new JSONParser();


        grabTechDetailsSpec myCallSpec = new grabTechDetailsSpec();
        try {
            myCallSpec.url = techsURL + URLEncoder.encode(techName, "utf-8");

        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "unable to encode technique! :O");
            return;
        }

        // Performs the upload.
        if (caller != null) {
            // There was already an upload in progress.
            caller.cancel(true);
        }
        caller = new ServerCall();
        caller.execute(myCallSpec);


    }

    class grabTechDetailsSpec extends ServerCallSpec {
        @Override
        public void useResult(Context context, String result) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                return;
            }
            try {
                // Getting JSON Array
                json = json.getJSONObject(jsonKey);

                String description = json.getString("description");
                String inputs = json.getString("inputs");
                String smashWiki = json.getString("smashwiki");



                TextView techDescription = (TextView) findViewById(R.id.techDescription);
                TextView techInputs = (TextView) findViewById(R.id.techInputs);
                TextView techWiki = (TextView) findViewById(R.id.techWiki);

                techDescription.setText(description);
                techInputs.setText(inputs);
                techWiki.setText(smashWiki);


                //addItems(tech);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void initType() {
        String fontPath = "fonts/Quicksand-Regular.ttf";

        // text view label

        TextView techTitle = (TextView) findViewById(R.id.techTitle);
        TextView techDescription = (TextView) findViewById(R.id.techDescription);
        TextView techInputs = (TextView) findViewById(R.id.techInputs);
        TextView techWiki = (TextView) findViewById(R.id.techWiki);

        TextView charsButton = (TextView) findViewById(R.id.charsListButton);

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font

        techTitle.setTypeface(tf);
        techTitle.setTextSize(20);

        techDescription.setTypeface(tf);
        techDescription.setTextSize(12);

        techInputs.setTypeface(tf);
        techInputs.setTextSize(8);

        techWiki.setTypeface(tf);
        techWiki.setTextSize(8);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tech_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
