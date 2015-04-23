package rsck.chalkboard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;

public class AddAssignment extends ActionBarActivity {

    private EditText assignmentName;
    private Spinner classSpinner;
    private String[] assignmentType;
    private EditText dueDate;
    private EditText totalPoints;
    private EditText notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        String chalkFontPath = "fonts/chalk_font.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);

        TextView title = (TextView) findViewById(R.id.add_assignment_title);
        assignmentType = getResources().getStringArray(R.array.assignment_type);
        classSpinner = (Spinner) findViewById(R.id.assignmentTypeSpinner);
        assignmentName = (EditText) findViewById(R.id.assignmentTextBox);
        dueDate = (EditText) findViewById(R.id.dateText);
        totalPoints = (EditText) findViewById(R.id.tPoints);
        notes = (EditText) findViewById(R.id.descriptionBox);

        title.setTypeface(tf);
        Button sendAssButtonClick = (Button) findViewById(R.id.addAssignment);

        sendAssButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String var;
                var = String.valueOf(classSpinner.getSelectedItem());
                onAddButtonClick(Arrays.toString(assignmentType),
                        String.valueOf(assignmentName),
                        String.valueOf(dueDate),
                        String.valueOf(totalPoints),
                        String.valueOf(notes));
            }
        });

        /*Creates a new ArrayAdapter, which binds each item in the string array to the initial
        appearance for the Spinner (which is how each item will appear in the spinner when selected)*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, assignmentType);
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

    protected void onAddButtonClick(String assignmentType,
                                    String assignmentName,
                                    String dueDate,
                                    String totalPoints,
                                    String notes){
        Intent ClassOverView = new Intent(this, ClassOverView.class);
        startActivity(ClassOverView);
    }

}
