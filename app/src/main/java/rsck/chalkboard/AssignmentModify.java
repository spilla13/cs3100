package rsck.chalkboard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;

public class AssignmentModify extends ActionBarActivity{

    private EditText currentGrade;
    private EditText totalGrade;
    private EditText category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button cancel = (Button) findViewById(R.id.cancel);
        Button update = (Button) findViewById(R.id.update);

        currentGrade = (EditText) findViewById(R.id.mGrade);
        totalGrade = (EditText) findViewById(R.id.maxGrade);
        category = (EditText) findViewById(R.id.mCategory); //turn to spinner
    }
}
