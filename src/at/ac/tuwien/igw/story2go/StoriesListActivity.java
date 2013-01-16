package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import at.ac.tuwien.igw.story2go.model.Story;

public class StoriesListActivity extends Activity {

	private ArrayList<Story> stories;

	private void initDatensaetze() {
		stories = new ArrayList<Story>();
		stories.add(new Story("Story1", "asdf", R.drawable.story_entry, 190));
		stories.add(new Story("Story1", "asdf", R.drawable.story2go_other, 1260));
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

		TextView greeting = (TextView) findViewById(R.id.greeting);
		greeting.setText("Hello " + SharedData.getUsername());
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			Story story = getItem(position);
			view.setId((int) getItemId(position));
			ImageView imageView = (ImageView) view.findViewById(R.id.icon);
			imageView.setImageResource(story.getImage());
			imageView.getLayoutParams().height = story.getImageHeight();
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position > 0)
				return;
			Story story = stories.get(position);
			Intent intent = new Intent(StoriesListActivity.this,
					MainActivity.class);
			startActivity(intent);
		}
	}
}
