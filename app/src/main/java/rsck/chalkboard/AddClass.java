package rsck.chalkboard;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;


public class AddClass extends ActionBarActivity {

    private Spinner classSpinner;
    private String[] classType;
    private EditText className;
    private EditText date;
    private EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_class);

        String chalkFontPath = "fonts/chalk_font.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);

        TextView title = (TextView) findViewById(R.id.Current_grade_text);
        classType = getResources().getStringArray(R.array.class_type);
        classSpinner = (Spinner) findViewById(R.id.classTypeSpinner);
        className = (EditText) findViewById(R.id.classNameTextBox);
        date = (EditText) findViewById(R.id.dateText);
        note = (EditText) findViewById(R.id.noteBox);

        title.setTypeface(tf);
        Button sendAddButtonClick = (Button) findViewById(R.id.addAClass);

        sendAddButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var;
                var = String.valueOf(classSpinner.getSelectedItem());
                onAddButtonClick(Arrays.toString(classType),
                        String.valueOf(note),
                        String.valueOf(className),
                        String.valueOf(date));
            }
        });

        /*Creates a new ArrayAdapter, which binds each item in the string array to the initial
        appearance for the Spinner (which is how each item will appear in the spinner when selected)*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(dataAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_class, menu);
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

    protected void onAddButtonClick(String classType,
                                    String note,
                                    String className,
                                    String date) {
        Intent Home = new Intent(this, Home.class);
        setResult(1, Home);
        finish();
    }
}
