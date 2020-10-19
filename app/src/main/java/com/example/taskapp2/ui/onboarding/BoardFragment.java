package com.example.taskapp2.ui.onboarding;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.taskapp2.Prefs;
import com.example.taskapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class BoardFragment extends Fragment {

    SpringDotsIndicator dotsIndicator;
    Button skip;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        skip = view.findViewById(R.id.skip);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
        final ViewPager viewPager = view.findViewById(R.id.viewPager);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            navController.navigate(R.id.phoneFragment);
        }
        BoardAdapter adapter = new BoardAdapter();
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        adapter.setOnStartClickListener(new BoardAdapter.OnStartClickListener() {
            @Override
            public void onStart() {
                Prefs.instance.saveShowState();
                navController.navigate(R.id.action_boardFragment_to_phoneFragment);

            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 2) {
                    skip.setVisibility(View.VISIBLE);
                } else {
                    skip.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}