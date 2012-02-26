package com.guantao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

public class AccountListActivity extends Activity
{
	private ListView listView;
	private List<String> dataList;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_list);
		
		dataList = new ArrayList<String>();
		dataList.add("博客园");
		dataList.add("博库书城");
		dataList.add("12306");
		listView = (ListView)findViewById(R.id.listView1);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList));
	}
}
