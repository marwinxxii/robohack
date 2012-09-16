package com.github.marwinxxii.robohack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

public class ConnService extends IntentService {

	public static final String NAME = "ConnService";

	public static final String FILENAME = "a.mp3";
	private static MediaPlayer mPlayer;

	public ConnService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd(FILENAME);
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(afd.getFileDescriptor());
			mPlayer.setLooping(true);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(NAME, e.getMessage());
			return;
		}
		serveBeep();
	}

	@SuppressWarnings("unused")
	private void serveCommands() {
		try {
			ServerSocket socket = new ServerSocket();
			WifiManager wfman = (WifiManager) getSystemService(WIFI_SERVICE);
			String address = Formatter.formatIpAddress(wfman
					.getConnectionInfo().getIpAddress());
			Log.d(NAME, address);
			socket.bind(new InetSocketAddress(address, 1409));
			boolean work = true;
			do {
				Socket conn = socket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String str;
				while ((str = reader.readLine()) != null) {
					if ("exit".equals(str)) {
						work = false;
						break;
					}
					Log.d(NAME, str);
					float volume = Float.parseFloat(str);
					mPlayer.setVolume(volume, volume);
				}
				reader.close();
				conn.close();
			} while (work);
			socket.close();
			Log.d(NAME, "Exiting");
		} catch (IOException e) {
			Log.e(NAME, e.getMessage());
		}
	}

	private void serveBeep() {
		int i = 0;
		while (i++ < 1000) {
			mPlayer.setVolume(1.0f, 1.0f);
			synchronized (this) {
				try {
					wait(500);
				} catch (InterruptedException e) {
					Log.e(NAME, e.getMessage());
				}
			}
			mPlayer.setVolume(0.0f, 0.0f);
			synchronized (this) {
				try {
					wait(500);
				} catch (InterruptedException e) {
					Log.e(NAME, e.getMessage());
				}
			}
		}
	}
}
