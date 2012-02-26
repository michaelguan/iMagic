package com.guantao;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author guantao baidumap key:D17D72725746793F37D7D03CFE29CF129575BCEB
 *
 */
public class IMagicActivity extends Activity
{
	private MediaPlayer defaultMediaPlayer = new MediaPlayer();
	Button btn_call;
	EditText phone_noEditText;
	EditText text_smsEditText;
	Button btn_sendsmsButton;
	Button btn_choise;
	private Vibrator vibrator;
	private LocationManager locationManager;
	private LocationListener listener;
	private Notification notification;
	private NotificationManager notificationManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		defaultMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		btn_call = (Button) findViewById(R.id.btn_call);
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		phone_noEditText = (EditText) findViewById(R.id.phone_no);
		text_smsEditText = (EditText) findViewById(R.id.text_sms);
		btn_call.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				make_call();
			}
		});

		btn_sendsmsButton = (Button) findViewById(R.id.btn_send_sms);
		btn_sendsmsButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				send_sms();
			}
		});

		btn_choise = (Button) findViewById(R.id.btn_chose);
		final CharSequence[] numbers =
		{ "520929", "13989452096", "07633696208", "13872185635", "13431884275" };
		btn_choise.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						IMagicActivity.this);
				builder.setTitle("请选择一个号码").setSingleChoiceItems(numbers, -1,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int item)
							{
								phone_noEditText.setText(numbers[item]);
								dialog.cancel();
							}
						});
				builder.create().show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu_mainop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_dial:
			make_call();
			break;
		case R.id.menu_sms:
			send_sms();
			break;
		case R.id.menu_play:
			play_music();
			break;
		case R.id.menu_about:
			show_about();
			break;
		case R.id.menu_options:
			op_setup();
			break;
		case R.id.menu_exit:
			exit_all();
			break;
		}
		return true;
	}

	private void make_call()
	{
		String tel_no = phone_noEditText.getText().toString();
		if (!tel_no.equals(""))
		{
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ tel_no));
			startActivity(callIntent);
		} else
		{
			Toast.makeText(this, "请输入要拨打的电话号码！", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 发送一条短信
	 * @param null
	 */
	private void send_sms()
	{
		String tel_no = phone_noEditText.getText().toString();
		String text_smsString = text_smsEditText.getText().toString();
		if (tel_no.equals(""))
		{
			Toast.makeText(this, "请输入要发送短信的电话号码！", Toast.LENGTH_SHORT).show();
		} else if (text_smsString.equals(""))
		{
			Toast.makeText(this, "不能发送空消息,请输入两个字吧!", Toast.LENGTH_SHORT).show();
		} else
		{
			SmsManager smsManager = SmsManager.getDefault();
			if (text_smsString.length() > 70)
			{
				List<String> mesStrings = smsManager
						.divideMessage(text_smsString);
				for (String content : mesStrings)
				{
					smsManager.sendTextMessage(tel_no, null, content, null,
							null);
				}
			} else
			{
				smsManager.sendTextMessage(tel_no, null, text_smsString, null,
						null);
			}
			Toast.makeText(this, "短信已发送 !", Toast.LENGTH_SHORT).show();
			text_smsEditText.setText("");
			
			notification = new Notification(R.drawable.icon,text_smsString, System.currentTimeMillis());
			notification.ledARGB=0xff0000ff;
			notification.ledOffMS=1000;
			notification.ledOnMS=500;
			notification.flags=Notification.FLAG_ONGOING_EVENT | Notification.FLAG_SHOW_LIGHTS | Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL ;
			notification.setLatestEventInfo(this, "发送了新的消息", text_smsString, PendingIntent.getActivity(this,0,new Intent(this,IMagicActivity.class),0));
			notificationManager.notify(2011, notification);
		}

	}

	private void play_music()
	{
		if (defaultMediaPlayer.isPlaying())
		{
			defaultMediaPlayer.pause();
			defaultMediaPlayer.seekTo(0);
			defaultMediaPlayer.start();
		} else
		{
			try
			{
				Uri mUri = Uri.parse("/sdcard/qqmusic/Suddenly.mp3");
				defaultMediaPlayer.setDataSource(getApplicationContext(), mUri);

				defaultMediaPlayer.prepare();
				defaultMediaPlayer.start();
				Toast.makeText(this, "开始播放歌曲", Toast.LENGTH_SHORT).show();

				defaultMediaPlayer
						.setOnCompletionListener(new OnCompletionListener()
						{
							public void onCompletion(MediaPlayer mp)
							{
								Toast.makeText(getApplicationContext(), "播放完成",
										Toast.LENGTH_SHORT).show();
							}
						});
			} catch (Exception e)
			{
				Toast.makeText(this, "找不到歌曲" + e.toString(), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void show_about()
	{
		//vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//vibrator.vibrate(new long[]
		//{ 100, 400, 100, 500, 200, 600 }, -1);
		Intent aIntent = new Intent(getApplication(),AccountListActivity.class);
		startActivity(aIntent);
	}

	private void op_setup()
	{
		/*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String privider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(privider);
		displayLocation(location);
		Toast.makeText(this, privider, Toast.LENGTH_LONG).show();
		listener = new LocationListener()
		{
			public void onStatusChanged(String provider, int status,
					Bundle extras)
			{
				// TODO Auto-generated method stub

			}

			public void onProviderEnabled(String provider)
			{
				// TODO Auto-generated method stub

			}

			public void onProviderDisabled(String provider)
			{
			}

			public void onLocationChanged(Location location)
			{
				displayLocation(location);
			}
		};
		locationManager.requestLocationUpdates(privider,
				6000, 10, listener);*/
		Intent mIntent = new Intent(getApplication(), BMapActivity.class);
		//mIntent.putExtra("Latitude", location.getLatitude());
		//mIntent.putExtra("Longitude", location.getLongitude());
		startActivity(mIntent);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (locationManager != null && listener != null)
		{
			locationManager.removeUpdates(listener);
		}
	}

	private void exit_all()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("退出确认").setMessage("确定要退出iMagic吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						IMagicActivity.this.finish();
						defaultMediaPlayer.release();
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which)
					{
						return;
					}
				});
		builder.create().show();
	}

}