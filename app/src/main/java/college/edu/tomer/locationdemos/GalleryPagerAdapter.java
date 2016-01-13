package college.edu.tomer.locationdemos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<String> images;

    public GalleryPagerAdapter(FragmentManager fm, ArrayList<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        MGalleryFragment fragment = MGalleryFragment.newInstance(images.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
