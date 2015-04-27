package rsck.chalkboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class AssignmentModify extends ActionBarActivity{

    private EditText mName;
    private EditText currentGrade;
    private EditText totalGrade;
    private Spinner classSpinner;
    private String[] classType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_assignment);

        Button sendCancelClick = (Button) findViewById(R.id.cancel);
        Button sendUpdateClick = (Button) findViewById(R.id.update);

        mName = (EditText) findViewById(R.id.mName);
        currentGrade = (EditText) findViewById(R.id.mGrade);
        totalGrade = (EditText) findViewById(R.id.maxGrade);
        classType = getResources().getStringArray(R.array.class_type);
        classSpinner = (Spinner) findViewById(R.id.classTypeSpinner);



        sendUpdateClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String.valueOf(classSpinner.getSelectedItem());
                onUpdateClick(String.valueOf(mName.getText()),
                        Arrays.toString(classType),
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

    }

    protected void onUpdateClick(String mName, String classType, String currentGrade, String totalGrade) {
        if(mName.length() >= 4 && mName.length() <= 100) {
            Intent ClassOverView = new Intent();

            setResult(RESULT_OK, ClassOverView);
            finish();
        }
        else if(mName.length() >= 4 && mName.length() <= 100)
            Toast.makeText(getApplicationContext(), "Name Too Short", Toast.LENGTH_SHORT).show();

        finish();
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
