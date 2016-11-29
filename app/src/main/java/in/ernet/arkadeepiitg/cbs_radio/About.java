package in.ernet.arkadeepiitg.cbs_radio;

/**
 * Created by arkadeep on 2/11/16.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView txtView = (TextView) findViewById(R.id.textView);
        txtView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView txtView1 = (TextView) findViewById(R.id.textView2);
        txtView1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView txtView2 = (TextView) findViewById(R.id.textView4);
        txtView2.setMovementMethod(LinkMovementMethod.getInstance());

    }
}