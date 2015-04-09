package smashlounge.smashlounge;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class characters extends Activity {
    private static final String TAG = "chars";
    private static final String charsURL = "http://dev.smashlounge.com/api/characters";
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
    private static String jsonKey = "characters";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techniques);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.techslist);


        jsonParser = new JSONParser();



        // Define a new List Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - the Array of data


        adapter = new CustomListAdapter(this , R.layout.chars_list , listItems);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        GrabCharSpec myCallSpec = new GrabCharSpec();
        myCallSpec.url = charsURL;

        // Performs the upload.
        if (caller != null) {
            // There was already an upload in progress.
            caller.cancel(true);
        }
        caller = new ServerCall();
        caller.execute(myCallSpec);


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

            }

        });

    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(String s) {
        listItems.add(s);
        adapter.notifyDataSetChanged();
    }

    class GrabCharSpec extends ServerCallSpec {
        @Override
        public void useResult(Context context, String result) {

            //this is where the API call returns with a string result
            //We need to parse it into JSON, now
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }
            try {

                // Getting JSON Array from result subarray
                techArr = json.getJSONArray(jsonKey);

                for (int i = 0; i < techArr.length()-1; i++) {
                    //grab each nested Char data
                    JSONObject c = techArr.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String tech = c.getString("name");
                    //appends this result to the ListView
                    addItems(tech);
                }


                //addItems(tech);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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

            TextView text = (TextView) mView.findViewById(R.id.charsTextView);

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
}