package rsck.chalkboard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Type;

public class ClassOverView extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.class_overview);


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
        TextView homework = (TextView) findViewById(R.id.homework_title);
        TextView tests = (TextView) findViewById(R.id.tests_title);
        TextView quizes = (TextView) findViewById(R.id.quizes_title);
        TextView projects = (TextView) findViewById(R.id.projects_title);
        TextView labs = (TextView) findViewById(R.id.labs_title);

        //Make the new typeface (font)
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);
        //Typeface rl = Typeface.createFromAsset(getAssets(), robotoFontPath);

        //Set the new typeface (font)
        currentGradeText.setTypeface(tf);
        currentGrade.setTypeface(tf);
        homework.setTypeface(tf);
        tests.setTypeface(tf);
        quizes.setTypeface(tf);
        projects.setTypeface(tf);
        labs.setTypeface(tf);
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

    protected void onAddAssignmentClick() {
        Intent AddAssignment = new Intent(this, AddAssignment.class);
        startActivity(AddAssignment);
    }

    protected void onHomeClick() {
        finish();
    }
}
