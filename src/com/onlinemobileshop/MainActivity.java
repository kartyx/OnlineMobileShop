package com.onlinemobileshop;

import com.onlinemobileshop.R;
import grandcentral.security.TokenAuthority;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.content.Intent;
 
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

private ViewPager viewPager;
private TabsPagerAdapter mAdapter;
private ActionBar actionBar;
private String[] tabs = {"Mobiles","Service"};

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

setContentView(R.layout.activity_main);
viewPager = (ViewPager) findViewById(R.id.pager);
actionBar = getSupportActionBar();
mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
try
{
viewPager.setAdapter(mAdapter);
}
catch(Exception e){
	Log.d("Debug",e.toString()); 
}
actionBar.setHomeButtonEnabled(true);

actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

// Adding Tabs
for (String tab_name : tabs) {
    actionBar.addTab(actionBar.newTab().setText(tab_name)
            .setTabListener(this));
}
viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	 
    @Override
    public void onPageSelected(int position) {
        // on changing the page
        // make respected tab selected
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
});
}
@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
    // on tab selected
    // show respected fragment view
    viewPager.setCurrentItem(tab.getPosition());
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater i=getMenuInflater();
	i.inflate(R.menu.menu, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch(item.getItemId())
	{
	case R.id.cartmenu: 
		Intent cartIntent=new Intent(getApplicationContext(),CartActivity.class);
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(cartIntent);
		break;
	case R.id.Help:
		Intent helpIntent=new Intent("com.onlinemobileshop.HELPACTIVITY");
	      helpIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	      startActivity(helpIntent);
	      break;
	case R.id.loginmenu:
		Intent loginIntent=new Intent("com.onlinemobileshop.LOGINACTIVITY");
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(loginIntent);
	case R.id.logoutmenu:
		Intent logoutIntent=new Intent("com.onlinemobileshop.LOGINACTIVITY");
		logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(logoutIntent);
	default:
	return super.onOptionsItemSelected(item);
	}
	return true;
}
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	if(WelcomeActivity.loggedin==true)
	{
	menu.findItem(R.id.loginmenu).setVisible(false);
	menu.findItem(R.id.logoutmenu).setVisible(true);
	}
	else
	{
		menu.findItem(R.id.loginmenu).setVisible(true);
		menu.findItem(R.id.logoutmenu).setVisible(false);
		}
	return super.onPrepareOptionsMenu(menu);
}

}