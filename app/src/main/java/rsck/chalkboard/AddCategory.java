package rsck.chalkboard;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddCategory extends DialogFragment implements View.OnClickListener{

    private EditText categoryName;
    private EditText categoryWeight;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_category, null);

        setCancelable(false);

        Button cancel = (Button) v.findViewById(R.id.cancel);
        Button addCategory = (Button) v.findViewById(R.id.addCategoryButton);
        categoryName = (EditText) v.findViewById(R.id.categoryTextBox);
        categoryWeight = (EditText) v.findViewById(R.id.categoryWeight);

        cancel.setOnClickListener(this);
        addCategory.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addCategoryButton){
            communicator.onDialogMessage(String.valueOf(categoryName.getText()),
                                         String.valueOf(categoryWeight.getText()));
            dismiss();
        }
        else{

            dismiss();
        }
    }

    interface Communicator{
            public void onDialogMessage(String categoryName, String categoryWeight);
    }
}
