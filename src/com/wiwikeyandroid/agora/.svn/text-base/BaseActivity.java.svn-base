package com.wiwikeyandroid.agora;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
public class BaseActivity extends FragmentActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }


    // Global view click listener
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onUserInteraction(view);
        }
    };


    public View.OnClickListener getViewClickListener(){
        return onClickListener;
    }

   
    public void onUserInteraction(View view){

    }

    public void log(Object obj) {

        // You can use filter *** to filter out message
//        LoggingUtils.error(getClass().getName(),
//                String.format("*** %s ***",
//                        obj == null ? "--!--"
//                                : obj.toString()));
    }
}
