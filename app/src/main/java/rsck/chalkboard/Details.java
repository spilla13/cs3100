package rsck.chalkboard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;


public class Details extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Button deleteB = (Button) findViewById(R.id.delete);
        Button modifyB = (Button) findViewById(R.id.modify);
        Button returnB = (Button) findViewById(R.id.returnButton);



    }
}
