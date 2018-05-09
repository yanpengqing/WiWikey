package com.wiwikeyandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author Joseph on 2016/5/28.
 *         <p/>
 */
public class MycallRecordsListView extends ListView {

	public MycallRecordsListView(Context context) {
		super(context);
	}

	public MycallRecordsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MycallRecordsListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);

	}
	
}
