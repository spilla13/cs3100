package rsck.chalkboard;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class Home extends Activity{
    private User user;

    //Constructor for activity
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //Set the font type
        String chalkFontPath = "fonts/chalk_font.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);

        Button sendAddButtonClick = (Button) findViewById(R.id.addButton);
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        TextView tv2 = (TextView) findViewById(R.id.textView2);

        //Get the username and password that was sent via the login screen
        Bundle bundle = getIntent().getExtras();

        user = bundle.getParcelable("user");

        //Change the text for both text views
        tv1.setText(user.getUserName());

        //connect the text view
        TextView currentClasses = (TextView) findViewById(R.id.Current_grade_text);

        //Set the new typeface (font)
        currentClasses.setTypeface(tf);

        sendAddButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add things to the action bar (if there is one)
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    //This should refresh the page when the user finishes the add calls
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {
            onRestart(); // your "refresh" code
        }
    }

    protected void onAddButtonClick() {
        Intent AddClass = new Intent(this, AddClass.class);
        startActivityForResult(AddClass, 1);
    }

    public User getUser(){
        return user;
    }
}
