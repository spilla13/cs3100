package rsck.chalkboard;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AssignmentFrag extends Fragment{

    public static AssignmentFrag newInstance(final Assignment assignment){
        AssignmentFrag f = new AssignmentFrag();
        Bundle b = new Bundle();
        b.putParcelable("assignment", assignment);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assignment_frag, container, false);

        TextView assignmentTitle = (TextView) view.findViewById(R.id.assignmentTitle);
        TextView assignmentGrade = (TextView) view.findViewById(R.id.assignmentGrade);

        Bundle bundle = getArguments();
        Assignment assignment = bundle.getParcelable("assignment");

        //change the text here!
        assignmentTitle.setText(assignment.name);
        assignmentGrade.setText(assignment.getLetterGrade());


        return view;
    }

}


