package rsck.chalkboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ClassOverView extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.class_overview);
        //Change the font of text
        //The font path
        String fontPath = "fonts/chalk_font.ttf";
        //Connect the text view
        TextView currentGradeText = (TextView) findViewById(R.id.Current_grade_text);
        //Make the new typeface (font)
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        //Set the new typeface (font)
        currentGradeText.setTypeface(tf);
    }
}
