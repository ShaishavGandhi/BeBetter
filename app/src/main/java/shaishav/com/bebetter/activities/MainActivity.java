package shaishav.com.bebetter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import javax.inject.Inject;

import shaishav.com.bebetter.R;
import shaishav.com.bebetter.controller.PickGoalController;
import shaishav.com.bebetter.controller.SummaryController;
import shaishav.com.bebetter.data.preferences.PreferenceDataStore;
import shaishav.com.bebetter.di.DependencyGraph;
import shaishav.com.bebetter.utils.BBApplication;

public class MainActivity extends AppCompatActivity {


  private Router router;
  @Inject PreferenceDataStore preferenceDataStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (getApplication() instanceof DependencyGraph) {
      ((BBApplication) getApplication()).getAppComponent().inject(this);
    }
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ViewGroup container = findViewById(R.id.container_body);

    router = Conductor.attachRouter(this, container, savedInstanceState);
    Controller rootController = getRootController();
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(rootController));
    }
  }

  private Controller getRootController() {
    if (preferenceDataStore.hasUserOnBoarded()) {
      return new SummaryController();
    }
    return new PickGoalController();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
