package rsck.chalkboard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddCategory extends Activity{

    private EditText categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        categoryName = (EditText) findViewById(R.id.categoryTextBox);
        Button sendAddCategoryClick = (Button) findViewById(R.id.addCategoryButton);
        Button sendCancelClick = (Button) findViewById(R.id.cancel);

        sendAddCategoryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCategoryClick(String.valueOf(categoryName.getText()));
            }
        });

        sendCancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });

    }

    protected void onAddCategoryClick(String categoryName) {
        Intent intent = new Intent();
        intent.putExtra("categoryName", categoryName);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onCancelClick() {
        Intent home = new Intent();
        setResult(RESULT_CANCELED, home);
        finish();
    }

    public void onBackPressed(){
        Intent home = new Intent();
        setResult(RESULT_CANCELED, home);
        super.onBackPressed();
    }
}
