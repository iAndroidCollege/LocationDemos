package college.edu.tomer.locationdemos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MGalleryFragment extends Fragment {
    private static final String ARG_IMAGE_URL = "url";
    @Bind(R.id.image)
    ImageView image;
    private String imageUrl;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageUrl image Url.
     * @return A new instance of fragment MGalleryFragment.
     */
    public static MGalleryFragment newInstance(String imageUrl) {
        MGalleryFragment fragment = new MGalleryFragment();
        //Add the parameters!
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mgallery, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(getActivity()).load(imageUrl).into(image);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
