package com.github.marwinxxii.robohack;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	public static final Float[] volumes = new Float[] { 0.0f, 0.4f, 0.5f,
			0.75f, 1.0f };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View v = findViewById(R.id.start);
		v.setOnClickListener(this);
		ListView lv = (ListView) findViewById(R.id.volumes);
		lv.setAdapter(new ArrayAdapter<Float>(this, R.layout.volume, volumes));
		lv.setOnItemClickListener(this);
		TextView tv = (TextView) findViewById(R.id.text_ip);
		tv.setText(getSharedPreferences("prefs", MODE_PRIVATE).getString(
				"host", ""));
	}

	public void onClick(View v) {
		startService(new Intent(this, ConnService.class));
		/*AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd(ConnService.FILENAME);
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(afd.getFileDescriptor());
			mPlayer.setLooping(true);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e("MainActivity", e.getMessage());
		}*/
	}

	public void onItemClick(AdapterView<?> adp, View view, int index, long id) {
		Intent i = new Intent(this, ClientService.class);
		i.putExtra("volume", volumes[index]);
		String host = ((TextView) findViewById(R.id.text_ip)).getText()
				.toString();
		getSharedPreferences("prefs", MODE_PRIVATE).edit()
				.putString("host", host).commit();
		i.putExtra("host", host);
		i.putExtra("port", 1409);
		startService(i);
	}
}
