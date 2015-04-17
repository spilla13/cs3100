package rsck.chalkboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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


        //Connect the text view
        //TextView cardTitles = (TextView) findViewById(R.id.card_title_id);
        TextView currentGradeText = (TextView) findViewById(R.id.Current_grade_text);
        TextView currentGrade = (TextView) findViewById(R.id.current_grade);
        TextView homework = (TextView) findViewById(R.id.homework_title);

        //Make the new typeface (font)
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);
        //Typeface rl = Typeface.createFromAsset(getAssets(), robotoFontPath);

        //Set the new typeface (font)
        currentGradeText.setTypeface(tf);
        currentGrade.setTypeface(tf);
        homework.setTypeface(tf);
        //cardTitles.setTypeface(rl);


    }
}
