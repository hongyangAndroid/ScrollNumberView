package com.zhy.t_sample_number_view;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhy.view.AutoScrollTextView;
import com.zhy.view.AutoScrollTextView.Mode;

public class ModeDownActivity extends Activity
{

	private AutoScrollTextView mAutoScrollViewNumberView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Mode:Down , 7->1");
		mAutoScrollViewNumberView = (AutoScrollTextView) findViewById(R.id.id_scroll_text);
		mAutoScrollViewNumberView.setMode(Mode.DOWN);
		mAutoScrollViewNumberView.setNumber(7);
		mAutoScrollViewNumberView.setTargetNumber(7);
		
		mAutoScrollViewNumberView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Random random = new Random();
				int start = random.nextInt(10);
				int end = random.nextInt(10);
				setTitle("Mode:Down , "+start+" -> " +end);
				mAutoScrollViewNumberView.setNumber(start);
				mAutoScrollViewNumberView.setTargetNumber(end);
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
