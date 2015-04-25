package rsck.chalkboard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClassOverView extends Activity {
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.class_overview);

        Bundle bundle = getIntent().getExtras();
        course = bundle.getParcelable("course");



        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.categoryMain);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.setId(12345);

        ArrayList<WeightedGrades> courseGrades = course.getGrades();

        for(WeightedGrades grades : courseGrades) {
            getFragmentManager().beginTransaction().add(ll.getId(), CategoryFrag.newInstance(grades), Integer.toString(grades.getID())).commit();
        }

        if(courseGrades.size() > 0)
            fragContainer.addView(ll);


        //Change the font of text
        //The font path
        String chalkFontPath = "fonts/chalk_font.ttf";
        String robotoFontPath = "fonts/roboto_light.ttf";

        Button sendAddAssignmentClick = (Button) findViewById(R.id.addAssignmentButton);
        Button sendHomeClick = (Button) findViewById(R.id.returnHomeButton);

        //Connect the text view
        //TextView cardTitles = (TextView) findViewById(R.id.card_title_id);
        TextView currentGradeText = (TextView) findViewById(R.id.Current_grade_text);
        TextView currentGrade = (TextView) findViewById(R.id.current_grade);

        //Make the new typeface (font)
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);
        //Typeface rl = Typeface.createFromAsset(getAssets(), robotoFontPath);

        //Set the new typeface (font)
        currentGradeText.setTypeface(tf);
        currentGrade.setTypeface(tf);

        sendAddAssignmentClick.setTypeface(tf);
        sendHomeClick.setTypeface(tf);
        //cardTitles.setTypeface(rl);

        sendAddAssignmentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddAssignmentClick();
            }
        });

        sendHomeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeClick();
            }
        });

    }

    //This should refresh the page when the user finishes the add calls
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final double pointsReceived;
        final double pointsPossible;
        final String assignmentName;
        final int catID;

        //TODO: Handle Result

        if(resultCode == RESULT_OK)
            if (requestCode == 1) {
                pointsReceived = intent.getDoubleExtra("pointsReceived", 0);
                pointsPossible = intent.getDoubleExtra("pointsPossible", 0);
                assignmentName = intent.getStringExtra("assignmentName");
                catID = intent.getIntExtra("catID",0);

                Thread t = new Thread(new Runnable() {
                    public void run() {
                    course.addHomeworkToCategory(pointsReceived, pointsPossible, assignmentName, catID);
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Fragment frag = getFragmentManager().findFragmentByTag(Integer.toString(catID));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag);
                ft.attach(frag);
                ft.commit();

                onRestart(); // your "refresh" code
            }
    }

    protected void onAddAssignmentClick() {
        Intent AddAssignment = new Intent(this, AddAssignment.class);
        AddAssignment.putParcelableArrayListExtra("weightedGrades", course.getGrades());
        startActivityForResult(AddAssignment, 1);
    }

    protected void onHomeClick() {
        Intent intent = new Intent();
        intent.putExtra("course", course);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBackPressed(){

        Intent intent = new Intent();
        intent.putExtra("course", course);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}
