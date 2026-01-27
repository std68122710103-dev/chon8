package com.pu.chon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingFragment extends Fragment {
    private int locationRequestCode = 1000;
    private Button button;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private double[] clat = {0};
    private double[] clng = {0};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingFragment newInstance(String param1, String param2) {
        settingFragment fragment = new settingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        button = (Button) view.findViewById(R.id.button);
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(callbackactive);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callbackactive);
                }
                Toast.makeText(getActivity(), "Get Your Location",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void Get_Current_location() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
// Got last known location. In some rare situations this can be
                        if (location != null) {
                            clat[0] = location.getLatitude();
                            clng[0] = location.getLongitude();

                        }
                    }
                });
    }
    private OnMapReadyCallback callbackactive = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Get_Current_location();
            googleMap.clear();
            String strtitle = "Chonlathi Location";
            String strsnippet = " อยู่นี้นะจ๊ะ " + clat[0] + ", " + clng[0];
            LatLng cposition = new LatLng(clat[0], clng[0]);
            MarkerOptions options = new MarkerOptions()
                    .position(cposition)
                    .title(strtitle)
                    .snippet(strsnippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

            LatLng cposition2 = new LatLng(46.65660985725523, 2.4743766862730383);
            MarkerOptions options2 = new MarkerOptions()
                    .position(cposition2)
                    .title("maker")
                    .snippet("เฮื้ยนข่อยเอง")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            googleMap.addMarker(options);
            googleMap.addMarker(options2);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cposition2, 15));
        }
    };
}