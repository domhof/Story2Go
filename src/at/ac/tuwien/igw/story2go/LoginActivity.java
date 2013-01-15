package at.ac.tuwien.igw.story2go;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set full screen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		final Button button = (Button) findViewById(R.id.buttonDone);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText usernameEdit = (EditText) findViewById(R.id.editTextUsername);
				SharedData.setUsername(usernameEdit.getText().toString());

				Intent intent = new Intent(LoginActivity.this,
						StoriesListActivity.class);
				startActivity(intent);
			}
		});
	}
}
