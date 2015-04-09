package smashlounge.smashlounge;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class loadingScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        initType();
    }
    public void loadChars(View view) {
        Intent intent = new Intent(this, characters.class);
        startActivity(intent);
    }
    public void loadTechs(View view) {
        Intent intent = new Intent(this, techniques.class);
        startActivity(intent);
    }

    public void initType() {
        String fontPath = "fonts/Quicksand-Regular.ttf";

        // text view label
        TextView slName = (TextView) findViewById(R.id.sl);
        TextView email = (TextView) findViewById(R.id.emailField);
        TextView pw = (TextView) findViewById(R.id.pwField);

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font
        slName.setTypeface(tf);
        email.setTypeface(tf);
        pw.setTypeface(tf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_screen, menu);
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
