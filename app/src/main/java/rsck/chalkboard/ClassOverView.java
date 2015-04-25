package rsck.chalkboard;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

        AssignmentFrag frag = new AssignmentFrag();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.categoryMain, frag, "DaPooPoo"); //tee hee
        transaction.commit();


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

        if (requestCode == 1) {
            onRestart(); // your "refresh" code
        }
    }

    protected void onAddAssignmentClick() {
        Intent AddAssignment = new Intent(this, AddAssignment.class);
        startActivityForResult(AddAssignment, 1);
    }

    protected void onHomeClick() {
        finish();
    }
}
