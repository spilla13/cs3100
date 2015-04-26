package rsck.chalkboard;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Toast;


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

        //Get the username and password that was sent via the login screen
        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");

        Thread t = new Thread(new Runnable() {
            public void run() {
                user.load();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClassTitleFragment frag = (ClassTitleFragment) getFragmentManager().findFragmentById(R.id.classOverList);
                        for(Course course: user.getCourses()){
                            frag.addElement(course.getCourseName());
                        }
                    }
                });
            }
        });
        t.start();

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
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        final String courseName;
        final String schoolName;
        if(resultCode == RESULT_OK) {
             courseName = intent.getStringExtra("courseName");
             schoolName = intent.getStringExtra("schoolName");
            if (requestCode == 1) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG);
                        user.addCourse(courseName, schoolName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ClassTitleFragment frag = (ClassTitleFragment) getFragmentManager().findFragmentById(R.id.classOverList);
                                frag.addElement(courseName);
                            }
                        });
                    }
                });
                t.start();
            }
        }
    }

    protected void onAddButtonClick() {
        Intent addClassIntent = new Intent(this, AddClass.class);
        addClassIntent.putExtra("courses", user.getCourses());
        startActivityForResult(addClassIntent, 1);
    }

    public User getUser(){
        return user;
    }

    public void updateUserCourse(Course course){
        user.updateCourse(course);
    }
}
