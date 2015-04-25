package rsck.chalkboard;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CategoryFrag extends android.app.Fragment {

    public static CategoryFrag newInstance(WeightedGrades grades){
        CategoryFrag f = new CategoryFrag();

        Bundle b = new Bundle();
        b.putParcelable("grades", grades);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_frag, container, false);

        TextView categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
        TextView categoryGrade = (TextView) view.findViewById(R.id.categoryGrade);
        TextView categoryPercent = (TextView) view.findViewById(R.id.categoryPercent);

        Bundle bundle = getArguments();
        WeightedGrades grades = bundle.getParcelable("grades");

        String title = grades.getName() + "(" + grades.getWeight() +")";
        double percentGrade = grades.weightedTotal()*100;
        String sPercentGrade = new BigDecimal(percentGrade).round(new MathContext(4, RoundingMode.HALF_UP)).toString();

        //change the text here!
        categoryTitle.setText(title);
        categoryGrade.setText(grades.getLetterGrade());
        categoryPercent.setText( sPercentGrade + "%");

        //This calls the assignment fragment
        LinearLayout fragContainer = (LinearLayout) view.findViewById(R.id.assignmentMain);
        LinearLayout cf = new LinearLayout(getActivity());
        cf.setOrientation(LinearLayout.VERTICAL);

        cf.setId(65401);


        for(Assignment assignment : grades.getAssignments())
            getFragmentManager().beginTransaction().add(cf.getId(),
                    AssignmentFrag.newInstance(assignment),
                    Integer.toString(assignment.ID)).commit();

        fragContainer.addView(cf);


        return view;
    }


}
