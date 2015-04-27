package rsck.chalkboard;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AssignmentFrag extends Fragment{

    Assignment assignment;
    String catName;

    public static AssignmentFrag newInstance(final Assignment assignment, String catName){
        AssignmentFrag f = new AssignmentFrag();
        Bundle b = new Bundle();
        b.putParcelable("assignment", assignment);
        b.putString("catName", catName);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assignment_frag, container, false);

        TextView assignmentTitle = (TextView) view.findViewById(R.id.assignmentTitle);
        TextView assignmentGrade = (TextView) view.findViewById(R.id.assignmentGrade);
        Button detailsButton = (Button) view.findViewById(R.id.assignmentDetails);

        detailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickShowDetails(v);
            }
        });

        Bundle bundle = getArguments();
        assignment = bundle.getParcelable("assignment");
        catName = bundle.getString("catName");

        //change the text here!
        assignmentTitle.setText(assignment.name);
        assignmentGrade.setText(assignment.getLetterGrade());


        return view;
    }

    public void onClickShowDetails(View v){
        FragmentManager manager = getActivity().getFragmentManager();

        Details myDialog = Details.newInstance(assignment, catName);
        myDialog.show(manager,"meow");
    }

}


