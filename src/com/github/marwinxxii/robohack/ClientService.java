package com.github.marwinxxii.robohack;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ClientService extends IntentService {

	public static final String NAME = "ClientService";

	public ClientService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent i) {
		float volume = i.getFloatExtra("volume", 0.0f);
		String host = i.getStringExtra("host");
		int port = i.getIntExtra("port", -1);
		try {
			Socket s = new Socket(host, port);
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.println(volume);
			writer.close();
			s.close();
		} catch (Exception e) {
			Log.d(NAME, e.getMessage());
		}
	}

}
