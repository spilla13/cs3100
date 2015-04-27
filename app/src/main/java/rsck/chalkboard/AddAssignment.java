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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AddAssignment extends ActionBarActivity {
    private EditText assignmentName;
    private Spinner classSpinner;
    private String[] assignmentType;
    private EditText pointsPossibleText;
    private EditText pointsReceivedText;
    private ArrayList<WeightedGrades> weightedGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);

        String chalkFontPath = "fonts/chalk_font.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), chalkFontPath);

        Bundle bundle = getIntent().getExtras();
        weightedGrades = bundle.getParcelableArrayList("weightedGrades");

        ArrayList<String> catNames = new ArrayList<>();
        for(WeightedGrades grade : weightedGrades)
            catNames.add(grade.getName());

        TextView title = (TextView) findViewById(R.id.add_assignment_title);
        assignmentType = getResources().getStringArray(R.array.assignment_type);
        classSpinner = (Spinner) findViewById(R.id.assignmentTypeSpinner);
        assignmentName = (EditText) findViewById(R.id.assignmentTextBox);
        pointsReceivedText = (EditText) findViewById(R.id.receivedPoints);
        pointsPossibleText = (EditText) findViewById(R.id.totalPoints);



        title.setTypeface(tf);
        Button sendAssButtonClick = (Button) findViewById(R.id.addAssignment);

        sendAssButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double pointsReceived;
                Double pointsPossible;

                if(pointsReceivedText.getText().length() > 0)
                    pointsReceived = Double.parseDouble(pointsReceivedText.getText().toString());
                else
                    pointsReceived = 0.0;

                if(pointsPossibleText.getText().length() > 0)
                    pointsPossible = Double.parseDouble(pointsPossibleText.getText().toString());
                else
                    pointsPossible = 0.0;

                if(pointsPossible < 0 )
                    pointsPossible = -pointsPossible;
                if(pointsReceived < 0)
                    pointsReceived = -pointsReceived;


                String var;
                var = String.valueOf(classSpinner.getSelectedItem());

                onAddButtonClick(classSpinner.getSelectedItemPosition(),
                        String.valueOf(assignmentName.getText()),
                        pointsPossible,
                        pointsReceived);
            }
        });

        /*Creates a new ArrayAdapter, which binds each item in the string array to the initial
        appearance for the Spinner (which is how each item will appear in the spinner when selected)*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, catNames);
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

    protected void onAddButtonClick(int assignmentType,
                                    String assignmentName,
                                    Double pointsPossible,
                                    Double pointsReceived){
        if(assignmentName.length() >= 4 && assignmentName.length() <= 100) {
            Intent ClassOverView = new Intent();
            //TODO:Handle other parts of the assignment
            ClassOverView.putExtra("pointsReceived", pointsReceived);
            ClassOverView.putExtra("pointsPossible", pointsPossible);
            ClassOverView.putExtra("assignmentName", assignmentName.replaceAll("\\s+",""));
            ClassOverView.putExtra("catID", weightedGrades.get(assignmentType).getID());
            setResult(RESULT_OK, ClassOverView);
            finish();
        }
        else if(assignmentName.length() >= 4 && assignmentName.length() <= 100)
            Toast.makeText(getApplicationContext(), "Name Too Short", Toast.LENGTH_SHORT).show();

    }

    public void onBackPressed(){
        Intent home = new Intent();
        setResult(RESULT_CANCELED, home);

        super.onBackPressed();
    }
}
