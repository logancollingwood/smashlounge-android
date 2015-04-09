package smashlounge.smashlounge;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class loadingScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

    public void loadTechList() {
        Intent intent = new Intent(this, techniques.class);
        startActivity(intent);
    }

    public void loadCharList() {
        Intent intent = new Intent(this, characters.class);
        startActivity(intent);
    }

    public void initType() {
        String fontPath = "fonts/Quicksand-Regular.ttf";

        // text view label

        TextView techsButton = (TextView) findViewById(R.id.techsListButton);
        TextView charsButton = (TextView) findViewById(R.id.charsListButton);

        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        // Applying font

        techsButton.setTypeface(tf);
        charsButton.setTypeface(tf);

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
                return true;
            case R.id.techniqueNav:
                Intent intent = new Intent(this, techniques.class);
                startActivity(intent);
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
