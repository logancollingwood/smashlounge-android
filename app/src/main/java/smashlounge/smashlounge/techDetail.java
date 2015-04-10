package smashlounge.smashlounge;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class techDetail extends ActionBarActivity {
    private static final String TAG = "techDetail";
    // Caller.
    private ServerCall caller;
    private JSONParser jsonParser;

    private static String techsURL = "http://dev.smashlounge.com/api/techniques?tech=";

    private static String jsonKey = "tech";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_detail);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //capture the parameter we sent, namely the technique name
        Bundle b = getIntent().getExtras();
        String techName = b.getString("key");

        //Set header of this activity to just be the technique name that was bundled with it
        TextView techTitle = (TextView) findViewById(R.id.techTitle);
        techTitle.setText(techName);

        jsonParser = new JSONParser();

        //sets up callback
        grabTechDetailsSpec myCallSpec = new grabTechDetailsSpec();

        try {
            //the url is just the base url with a _GET param for the server
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
            JSONObject techJson = null;

            JSONArray gifJson = null;

            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                return;
            }
            try {
                // Getting JSON Array
                techJson = json.getJSONObject(jsonKey);

                String description = techJson.getString("description");
                String inputs = "inputs: ";
                inputs += techJson.getString("inputs");
                String smashWiki = techJson.getString("smashwiki");


                TextView techDescription = (TextView) findViewById(R.id.techDescription);
                TextView techInputs = (TextView) findViewById(R.id.techInputs);
                TextView techWiki = (TextView) findViewById(R.id.techWiki);

                techDescription.setText(description);
                techInputs.setText(inputs);
                techWiki.setText(smashWiki);

                //Now let's grab gifs!
                gifJson = json.getJSONArray("gifs");

                Log.d(TAG, "gif JSON: " + gifJson.toString());
                for (int i = 0; i <= gifJson.length()-1; i++) {
                    // Storing  JSON item in a Variable
                    JSONObject c = gifJson.getJSONObject(i);

                    String url = c.getString("url");
                    String gifDesc = c.getString("description");

                    String webmUrl = "http://zippy.gfycat.com/" + url + ".webm";
                    Log.d(TAG, url + " " + gifDesc);
                    MediaPlayer mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(webmUrl);
                        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });

                    } catch (IOException e) {
                        Log.d(TAG, "unable to play webm");
                        return;
                    }


                }

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
