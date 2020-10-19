package com.example.taskapp2.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {

    private EditText editCode;
    private TextView timer;
    EditText editPhone;
    Button continue_btn;
    TextView check;
    Button confirm;
    private String verificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.edit_phone);
        editCode = view.findViewById(R.id.sms_code);
        continue_btn = view.findViewById(R.id.btn_continue);
        check = getView().findViewById(R.id.check);
        check.setVisibility(View.GONE);
        timer = view.findViewById(R.id.timer);
        confirm = view.findViewById(R.id.confirm);
        initCallbacks();
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSms();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editCode.getText().toString().trim();
                if (!code.isEmpty()) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    signIn(phoneAuthCredential);
                }
            }
        });

        view.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSms();
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void startTimer() {
        new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("У вас осталось " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(requireActivity(), "Время вышло!", Toast.LENGTH_SHORT).show();
                openNumber();
            }
        }.start();
    }

    private void openNumber() {
        editPhone.setVisibility(View.VISIBLE);
        continue_btn.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
        editCode.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        check.setVisibility(View.VISIBLE);
    }


    private void initCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                String sms = phoneAuthCredential.getSmsCode();
                if (sms != null) {
                    editCode.setText(sms);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openHome();
                }
            }
        });
    }


    private void requestSms() {
        String phone = editPhone.getText().toString().trim();
        if (!phone.isEmpty()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, requireActivity(), callbacks);
            editPhone.setVisibility(View.GONE);
            continue_btn.setVisibility(View.GONE);
            check.setVisibility(View.GONE);
            timer.setVisibility(View.VISIBLE);
            editCode.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
        }
        startTimer();

    }

    private void openHome() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_phoneFragment_to_navigation_home);
    }
}