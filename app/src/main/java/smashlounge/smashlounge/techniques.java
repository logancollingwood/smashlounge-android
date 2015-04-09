package smashlounge.smashlounge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class techniques extends ActionBarActivity {
    private static final String TAG = "techs";
    private static final String techsURL = "http://dev.smashlounge.com/api/techniques";
    // Caller.
    private ServerCall caller;

    JSONArray techArr = null;

    ListView listView ;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    private JSONParser jsonParser;
    //10.0.2.2 is the address used by the Android emulators to refer to the host address
    // change this to the IP of another host if required

    private static String jsonResult = "success";
    private static String jsonKey = "tech";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techniques);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.techslist);


        jsonParser = new JSONParser();


        GrabTechniqueSpec myCallSpec = new GrabTechniqueSpec();
        myCallSpec.url = techsURL;

        // Performs the upload.
        if (caller != null) {
            // There was already an upload in progress.
            caller.cancel(true);
        }
        caller = new ServerCall();
        caller.execute(myCallSpec);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - the Array of data

        adapter = new CustomListAdapter(this , R.layout.techs_list , listItems);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(techniques.this, techDetail.class);

                //Lets throw the technique name in the intent so we can use it to display details
                Bundle b = new Bundle();
                b.putString("key", itemValue);
                intent.putExtras(b); //package with intent

                //load it up!
                startActivity(intent);
                finish();
            }

        });

    }
    class GrabTechniqueSpec extends ServerCallSpec {
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
                Log.d(TAG, json.toString());
                techArr = json.getJSONArray(jsonKey);

                for (int i = 0; i < techArr.length()-1; i++) {
                    JSONObject c = techArr.getJSONObject(i);
                    // Storing  JSON item in a Variable

                    String description = c.getString("description");
                    String tech = c.getString("tech");
                    String inputs = c.getString("inputs");

                    addItems(tech);
                }


                //addItems(tech);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(String s) {
        listItems.add(s);
        adapter.notifyDataSetChanged();
    }


    private class CustomListAdapter extends ArrayAdapter {

        String fontPath = "fonts/Quicksand-Regular.ttf";
        private Context mContext;
        private int id;
        private List<String> items ;

        public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
        {
            super(context, textViewResourceId, list);
            mContext = context;
            id = textViewResourceId;
            items = list ;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent)
        {
            View mView = v ;
            if(mView == null){
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = vi.inflate(id, null);
            }

            TextView text = (TextView) mView.findViewById(R.id.techsTextView);

            if(items.get(position) != null )
            {
                // Loading Font Face
                Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

                text.setTextColor(Color.WHITE);
                text.setText(items.get(position));
                text.setTextSize(24);
                text.setTypeface(tf);
            }

            return mView;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_loading_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.sl_brand:
                Intent homeIntent = new Intent(this, loadingScreen.class);
                startActivity(homeIntent);
                return true;
            case R.id.techniqueNav:
                return true;
            case R.id.charNav:
                Intent charIntent = new Intent(this, characters.class);
                startActivity(charIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}