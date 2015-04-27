package rsck.chalkboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AssignmentModify extends ActionBarActivity{

    private EditText mName;
    private EditText currentGrade;
    private EditText totalGrade;
    //private Spinner catSpinner;
    private String[] classType;
    private ArrayAdapter<String> dataAdapter;
    private ArrayList<WeightedGrades> weightedGrades;
    private Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_assignment);

        Button sendCancelClick = (Button) findViewById(R.id.cancel);
        Button sendUpdateClick = (Button) findViewById(R.id.update);

        Bundle bundle = getIntent().getExtras();
        weightedGrades = bundle.getParcelableArrayList("weightedGrades");
        assignment = bundle.getParcelable("assignment");

        ArrayList<String> catNames = new ArrayList<>();
        for(WeightedGrades grade : weightedGrades)
            catNames.add(grade.getName());

        catNames.add("No Change");


        mName = (EditText) findViewById(R.id.mName);
        currentGrade = (EditText) findViewById(R.id.mGrade);
        totalGrade = (EditText) findViewById(R.id.maxGrade);
        classType = getResources().getStringArray(R.array.class_type);
        //catSpinner = (Spinner) findViewById(R.id.classTypeSpinner);



        sendUpdateClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClick(String.valueOf(mName.getText()),
                        /*catSpinner.getSelectedItemPosition(),*/
                        String.valueOf(currentGrade.getText()),
                        String.valueOf(totalGrade.getText()));
            }
        });

        sendCancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });

        /*Initialize spinner
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, catNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(dataAdapter);*/

    }

    protected void onUpdateClick(String mName, /*int catPosition,*/
                                 String pointsReceivedText, String pointsPossibleText) {

        if((mName.length() >= 4 && mName.length() <= 100) || mName.length() == 0) {
            Intent ClassOverView = new Intent();



            if(mName.length() > 0)
                assignment.name = mName;
            //if (!dataAdapter.getItem(catPosition).equals("No Change"))
            //    ClassOverView.putExtra("catID", weightedGrades.get(catPosition).getID());
            if (dataAdapter.getItem(catPosition) != "No Change")
                ClassOverView.putExtra("catID", weightedGrades.get(catPosition).getID());

            if (pointsReceivedText.length() > 0) {
                Double pointsReceived = Double.parseDouble(pointsReceivedText);
                assignment.pointsReceived = pointsReceived;
            }
            if (pointsPossibleText.length() > 0) {
                Double pointsPossible = Double.parseDouble(pointsPossibleText);
                assignment.pointsPossible = pointsPossible;
            }
            ClassOverView.putExtra("assignment", assignment);

            setResult(RESULT_OK, ClassOverView);
            finish();
        }
        else if(mName.length() < 4 || mName.length() > 100)
            Toast.makeText(getApplicationContext(), "Name Too Short", Toast.LENGTH_SHORT).show();

    }

    protected void onCancelClick() {

        finish();
    }

    public void onBackPressed(){
        Intent home = new Intent();
        setResult(RESULT_CANCELED, home);

        super.onBackPressed();
    }


}
