package com.example.taskapp2.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.taskapp2.R;

public class BoardAdapter extends PagerAdapter {
    Button button;
    private String[] titles = new String[]{"Fast", "Free", "Powerful"};
    private String[] desks = new String[]{"This App delivers messages faster that any other applications",
            "Don't pay for anything! It's totally free", "Use the most powerful App in 21st century!"};
    private int[] images = new int[]{R.drawable.viewpager_1, R.drawable.wallet, R.drawable.wallet};

    private OnStartClickListener onStartClickListener;

    public void setOnStartClickListener(OnStartClickListener onStartClickListener) {
        this.onStartClickListener = onStartClickListener;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.page_board, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textTitle = view.findViewById(R.id.textTitle);
        TextView textDesk = view.findViewById(R.id.textDesk);
        button = view.findViewById(R.id.btn_getStarted);
        if(position >=2){
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartClickListener.onStart();
            }
        });
        textTitle.setText(titles[position]);
        textDesk.setText(desks[position]);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnStartClickListener{
        void onStart();
    }

}
