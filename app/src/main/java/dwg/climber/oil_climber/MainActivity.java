package dwg.climber.oil_climber;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentPagerItemAdapter adapter;
    String[] celeb_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.hot, Hot_Fragment.class)
                .add(R.string.comment, Comment_Fragment.class)
                .create());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        Resources res = getResources();
        celeb_profile= res.getStringArray(R.array.celeb_profile);
        set_profile_picture(celeb_profile[0]);
    }

    public void set_profile_picture(String url_i){
        final ImageView img = (ImageView) (findViewById(R.id.image_view));
        Glide.with(this).load(url_i).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
    public void prepareData_profile(){
        //given data
        //String url_i = "http://image.hankookilbo.com/i.aspx?Guid=5f5962508457411cb1882a0747acb9a0&Month=201603&size=640";
        String url_i = "http://cfile9.uf.tistory.com/T750x750/2519E434561A7AF4291F76";
        int r_num = 13030;
        //set_profile_picture(celeb_profile[0]);
        final ImageView img = (ImageView) (findViewById(R.id.image_view));
        Glide.with(this).load(url_i).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
        TextView num = (TextView)(findViewById(R.id.num_recommend));
        num.setText(String.valueOf(r_num));
    }

    public void drawer_togle(View v){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerVisible(GravityCompat.START)) drawer.closeDrawers();
        else drawer.openDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void appbar_click(View v){
        View lay = (View) this.findViewById(R.id.image_display_layout);
        if(lay.getVisibility()==View.VISIBLE) lay.setVisibility(View.GONE);
        else lay.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.image_search) {
            Intent i = new Intent(MainActivity.this, Image_Search_Activity.class);
            startActivity(i);
        }
        else if(id == R.id.add_following) {
            Intent i = new Intent(MainActivity.this, FollowingActivity.class);
            startActivity(i);
        }
        else if(id == R.id.following_list) {
            Intent i = new Intent(MainActivity.this, FollowingListActivity.class);
            startActivityForResult(i, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int c_id = data.getIntExtra("c_id",0);
                Hot_Fragment hot_fragment = (Hot_Fragment) adapter.getPage(0);
                hot_fragment.get_data_from_server(c_id);
                Comment_Fragment comment_fragment = (Comment_Fragment) adapter.getPage(1);
                comment_fragment.get_data_from_server(c_id);
                set_profile_picture(celeb_profile[c_id-1]);
            }
        }
    }
}
