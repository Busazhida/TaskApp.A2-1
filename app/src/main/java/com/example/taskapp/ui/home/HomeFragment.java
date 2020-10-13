package com.example.taskapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.App;
import com.example.taskapp.R;
import com.example.taskapp.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TaskAdapter adapter;
    private FloatingActionButton fab;
    private ArrayList<Task> list = new ArrayList<>();
    private RecyclerView recyclerView;
    public boolean knopka = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter(list);
        List<Task> list = App.instance.getAppDatabase().taskDao().getAll();
        adapter.setList(list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerView);
        initListeners();
        initList();

    }

    private void initList() {
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        adapter.setListener(new TaskAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", list.get(position));
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_navigation_home_to_formFragment, bundle);
                getParentFragmentManager().setFragmentResultListener("updateform", HomeFragment.this, new FragmentResultListener() {

                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        adapter.update(position, (Task) result.getSerializable("update"));
                    }
                });
            }

            @Override
            public void onLongClick(final int position) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                adapter.deleteItem(position);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete?")
                        .setPositiveButton("YES", dialogClickListener)
                        .setNegativeButton("NO", dialogClickListener).show();
            }
        });

    }

    private void initListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForm();
            }
        });
        getParentFragmentManager().setFragmentResultListener("form", HomeFragment.this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("TAG", "onFragmentResult:" + result.getString("task"));
                adapter.addItem((Task) result.getSerializable("task"));
            }
        });
    }

    private void openForm() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_navigation_home_to_formFragment);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_menu:

                list.clear();
                if (knopka) {
                    list.addAll(App.getInstance().getAppDatabase().taskDao().getTaskDateAlphabetically(knopka));
                    knopka = false;
                } else {
                    list.addAll(App.getInstance().getAppDatabase().taskDao().getTaskDateAlphabetically(knopka));
                    knopka = true;
                }
                adapter.notifyDataSetChanged();
                return true;

            case R.id.sort:
                sorted();
                break;

            case R.id.delete:
                list.clear();
                alert();
                App.getInstance().getAppDatabase().taskDao().someList();
                list.addAll(App.instance.getAppDatabase().taskDao().getAll());
        }
        return true;
    }
    private boolean data = true;
    private void sorted() {
        list.clear();
        if (data) {
            list.addAll(App.getInstance().getAppDatabase().taskDao().getTasksAlphabetically(data));
            data = false;
        } else {
            list.addAll(App.getInstance().getAppDatabase().taskDao().getTasksAlphabetically(data));
            data =true;
        }
        adapter.notifyDataSetChanged();
    }

    private void alert() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        list.clear();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete?")
                .setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();
    }

}


