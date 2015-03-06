package com.zhy.t_sample_number_view;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends ListActivity
{

	private String[] list = { "MODE_DOWN", "MODE_UP", "MODE_DOWN_AND_UP" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("choose mode:");

		getListView().setAdapter(
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, list));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = null;
		switch (position)
		{
		case 0:
			intent = new Intent(this, ModeDownActivity.class);
			break;
		case 1:

			intent = new Intent(this, ModeUpActivity.class);
			break;
		case 2:
			intent = new Intent(this, ModeDownAndUpActivity.class);
			break;
		}

		if (intent != null)
			startActivity(intent);
	}

}
