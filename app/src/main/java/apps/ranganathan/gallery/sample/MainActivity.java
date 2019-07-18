package apps.ranganathan.gallery.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import apps.ranganathan.gallery.R;
import apps.ranganathan.gallery.sample.expand.ExpandActivity;
import apps.ranganathan.gallery.sample.multicheck.MultiCheckActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button expand = (Button) findViewById(R.id.expand_button);
    expand.setOnClickListener(navigateTo(ExpandActivity.class));

    Button multiSelect = (Button) findViewById(R.id.multi_check_button);
    multiSelect.setOnClickListener(navigateTo(MultiCheckActivity.class));



  }

  public OnClickListener navigateTo(final Class<?> clazz) {
    return new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, clazz);
        startActivity(intent);
      }
    };
  }
}
