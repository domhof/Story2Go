package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import at.ac.tuwien.igw.story2go.model.Story;

public class StoriesListActivity extends Activity {

	private ArrayList<Story> stories;

	private void initDatensaetze() {
		stories = new ArrayList<Story>();
		stories.add(new Story(
				"Story1",
				"Beschreibung asdfasdfasdfasdasasdfasdfasdfasdasasdfasdfasdfasdasasdfasdfasdfasdas"));
	}

	private StoriesListItemAdapter myAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		setContentView(R.layout.stories_list);
		initDatensaetze();
		ListView l = (ListView) findViewById(R.id.storiesList);
		myAdapter = new StoriesListItemAdapter();
		l.setAdapter(myAdapter);
		l.setOnItemClickListener(myAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// falls wir vom �nderungsformular zur�ckkommen...
		myAdapter.notifyDataSetChanged();
	}

	public class StoriesListItemAdapter extends BaseAdapter implements
			OnItemClickListener {
		private final LayoutInflater mInflater;

		public StoriesListItemAdapter() {
			mInflater = (LayoutInflater) StoriesListActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return stories.size();
		}

		public Story getItem(int position) {
			return stories.get(position);
		}

		public long getItemId(int position) {
			return (long) position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout itemView = (LinearLayout) mInflater.inflate(
					R.layout.stories_list_item, parent, false);
			bindView(itemView, position);
			return itemView;
		}

		private void bindView(LinearLayout view, int position) {
			Story datensatz = getItem(position);
			view.setId((int) getItemId(position));
			ImageView nummerTextView = (ImageView) view.findViewById(R.id.icon);
			nummerTextView.setImageResource(R.drawable.story_entry);
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Meldung ausgeben oder Intent bauen und Activity starten
			Story gewaehlterDatensatz = stories.get(position);
		}
	}
}