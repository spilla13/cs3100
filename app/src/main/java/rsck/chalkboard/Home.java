package rsck.chalkboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Home extends Activity{
    //Constructor for activity
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        //Set the font type
        String chalkFontPath = "fonts/chalk_font.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);

        Button sendClassOverView = (Button) findViewById(R.id.classOverViewButton);
        Button sendAddButtonClick = (Button) findViewById(R.id.addButton);
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        TextView tv2 = (TextView) findViewById(R.id.textView2);

        //Get the username and password that was sent via the login screen
        String username = getIntent().getStringExtra("theUser");
        String password = getIntent().getStringExtra("password");

        //Change the text for both text views
        tv1.setText(username);
        tv2.setText(password);

        //connect the text view
        TextView currentClasses = (TextView) findViewById(R.id.Current_grade_text);
        TextView buttonClassOverview = (TextView) findViewById(R.id.classOverViewButton);
        //Set the new typeface (font)
        currentClasses.setTypeface(tf);
        buttonClassOverview.setTypeface(tf);

        sendClassOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send to class overview activity
                sendToClassOverView();
            }
        });

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

    protected void sendToClassOverView(){
        Intent classOverView = new Intent(this, ClassOverView.class);
        startActivity(classOverView);
    }


    protected void onAddButtonClick() {
        Intent AddClass = new Intent(this, AddClass.class);
        startActivity(AddClass);
    }
}
