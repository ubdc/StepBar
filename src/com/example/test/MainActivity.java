package com.example.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();
		fm.beginTransaction()
		.add(R.id.container, new A())
		.commit();
	}
	
	class A extends Fragment implements OnClickListener {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView v = new TextView(getActivity());
			v.setText("AAAAA");
			v.setBackgroundColor(0x80ff0000);
			v.setOnClickListener(this);
			return v;
		}

		@Override
		public void onClick(View v) {
			fm.beginTransaction()
			.setCustomAnimations(
					R.anim.slide_in, //新的fragment进入动画
					R.anim.slide_out, //当前fragment移入stack(或直接移除)时动画
					R.anim.pop_slide_in, //旧的fragment从stack中pop出来的动画
					R.anim.pop_slide_out) //当前fragment从stack移除动画
			.replace(R.id.container, new B())
			.addToBackStack(null)
			.commit();
		}
	}
	
	class B extends Fragment implements OnClickListener {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView v = new TextView(getActivity());
			v.setText("BBBBB");
			v.setBackgroundColor(0x8000ff00);
			v.setOnClickListener(this);
			return v;
		}

		@Override
		public void onClick(View v) {
			fm.beginTransaction()
			.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
			.replace(R.id.container, new C())
			.addToBackStack(null)
			.commit();
		}
	}
	
	class C extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView v = new TextView(getActivity());
			v.setText("CCCCC");
			v.setBackgroundColor(0x800000ff);
			return v;
		}
	}
}
