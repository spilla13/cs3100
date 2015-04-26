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

    public static final int HW_FRAG_ID = 2;
    private WeightedGrades weightedGrades;

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
        weightedGrades = bundle.getParcelable("grades");

        String title = weightedGrades.getName() + "(" + weightedGrades.getWeight() +")";
        double percentGrade = weightedGrades.weightedTotal()*100;
        String sPercentGrade = new BigDecimal(percentGrade).round(new MathContext(4, RoundingMode.HALF_UP)).toString();

        //change the text here!
        categoryTitle.setText(title);
        categoryGrade.setText(weightedGrades.getLetterGrade());
        categoryPercent.setText( sPercentGrade + "%");

        //This calls the assignment fragment
        LinearLayout fragContainer = (LinearLayout) view.findViewById(R.id.assignmentMain);
        LinearLayout cf = new LinearLayout(getActivity());
        cf.setOrientation(LinearLayout.VERTICAL);

        cf.setId(HW_FRAG_ID);

        for(Assignment assignment : weightedGrades.getAssignments())
            getChildFragmentManager().beginTransaction().add(cf.getId(),
                    AssignmentFrag.newInstance(assignment),
                    Integer.toString(assignment.ID)).commit();

        fragContainer.addView(cf);

        return view;
    }


}
