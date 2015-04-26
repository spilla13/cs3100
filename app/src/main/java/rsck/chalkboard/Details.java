package rsck.chalkboard;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Details extends DialogFragment implements View.OnClickListener{

    public TextView assignmentTitle;
    public TextView cGrade;
    public TextView maxGrade;
    public TextView cPercent;
    public TextView cCategory;
    public TextView cSchool;
    public TextView cClass;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details, null);

        Button deleteB = (Button) v.findViewById(R.id.delete);
        Button modifyB = (Button) v.findViewById(R.id.modify);
        Button returnB = (Button) v.findViewById(R.id.returnButton);
        assignmentTitle = (TextView) v.findViewById(R.id.assignmentTitle);
        cGrade = (TextView) v.findViewById(R.id.cGrade);
        maxGrade = (TextView) v.findViewById(R.id.maxGrade);
        cPercent = (TextView) v.findViewById(R.id.cPercent);
        cCategory = (TextView) v.findViewById(R.id.cCategory);
        cSchool = (TextView) v.findViewById(R.id.cSchool);
        cClass = (TextView) v.findViewById(R.id.cClass);




        //TODO:Get Details from Calling Activity.
        assignmentTitle.setText("");
        cGrade.setText("95");
        maxGrade.setText("101");
        cPercent.setText("94%");
        cCategory.setText("Homework");
        cSchool.setText("Star Fleet Enterprise");
        cClass.setText("Software Design for Spaceships");

        modifyB.setOnClickListener(this);
        deleteB.setOnClickListener(this);
        returnB.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.modify){
            communicator.onDetailsMessage(String.valueOf(assignmentTitle),"modify"); //this should return the assignment name so you can delete it
            dismiss();
        }
        else if(v.getId() == R.id.delete){
            communicator.onDetailsMessage(String.valueOf(assignmentTitle), "delete");
            dismiss();
        }
        else{
            dismiss();
        }

    }

    interface Communicator{
        public void onDetailsMessage(String assignmentTitle, String method);
    }
}
