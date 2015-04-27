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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class Details extends DialogFragment implements View.OnClickListener{

    public TextView assignmentTitle;
    public TextView cGrade;
    public TextView maxGrade;
    public TextView cPercent;
    public TextView cCategory;
    public TextView cSchool;
    public TextView cClass;
    Communicator communicator;
    Assignment assignment;

    public static Details newInstance(Assignment assignment, String catName){
        Details deets = new Details();
        Bundle args = new Bundle();
        args.putParcelable("assignment", assignment);
        args.putString("catName",catName);
        deets.setArguments(args);

        return deets;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details, null);

        Bundle args = getArguments();

        assignment = args.getParcelable("assignment");
        String catName = args.getString("catName");

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

        double percentGrade = assignment.pointsReceived/assignment.pointsPossible * 100;
        String sPercentGrade = new BigDecimal(percentGrade).round(new MathContext(4, RoundingMode.HALF_UP)).toString();


        Course course = ((ClassOverView) getActivity()).getCourse();

        //TODO:Get Details from Calling Activity.
        getDialog().setTitle(assignment.name); //sets the title of the Dialog
        cGrade.setText(String.valueOf(assignment.pointsReceived));
        maxGrade.setText(String.valueOf(assignment.pointsPossible));
        cPercent.setText(sPercentGrade + "%");
        cCategory.setText(catName);
        cSchool.setText(course.getSchoolName());
        cClass.setText(course.getCourseName());

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
