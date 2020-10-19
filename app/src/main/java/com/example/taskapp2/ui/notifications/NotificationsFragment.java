package com.example.taskapp2.ui.notifications;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taskapp2.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NotificationsFragment extends Fragment {
    private EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        save();
        editText = view.findViewById(R.id.edtText);
        openText();
    }

    private void save() {
        Permissions.check(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                createFile();
            }

            private void createFile() {
                File folder = new File(Environment.getExternalStorageDirectory(), "TaskApp");
                folder.mkdir();
                File file = new File(folder, "note.txt");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void openText(){
        FileInputStream fin = null;
        File file = new File(Environment.getExternalStorageDirectory(), "note.txt");

        try {
            fin =  new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            editText.setText(text);
        }
        catch(IOException ex) {
            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveText(String save_text){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "note.txt"));
            fos.write(save_text.getBytes());
        }
        catch(IOException ex) {
            Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Toast.makeText(requireActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveText(editText.getText().toString());
    }
}