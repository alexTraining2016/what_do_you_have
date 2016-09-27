package comalexpolyanskyi.github.what_do_you_have;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToSecondBtn(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
