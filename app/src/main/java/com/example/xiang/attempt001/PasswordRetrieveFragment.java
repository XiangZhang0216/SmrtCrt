package com.example.xiang.attempt001;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rx.subscriptions.CompositeSubscription;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiang on 2017-06-19.
 */

public class PasswordRetrieveFragment extends Fragment {
    View myView;
    public static final String TAG = PasswordRetrieveFragment.class.getSimpleName();
    private TextInputLayout emailLayout;
    private TextInputLayout tokenLayout;
    private TextInputLayout passwordLayout;
    private EditText emailInput;
    private EditText tokenInput;
    private EditText passwordInput;
    private TextView retrieveMessage;
    private Button retrieveButton;
    private ProgressBar retrieveProgress;
    private CompositeSubscription subscription;
    private String myEmail;
    private boolean isInit  = true;
    private Listener myListener;
    public interface Listener
    {
        void onPasswordReset(String message);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_password_retrieve,container,false);
        subscription = new CompositeSubscription();
        emailLayout = (TextInputLayout)myView.findViewById(R.id.ti_retrieve_email);
        tokenLayout = (TextInputLayout)myView.findViewById(R.id.ti_retrieve_token);
        passwordLayout = (TextInputLayout)myView.findViewById(R.id.ti_retrieve_password);
        emailInput = (EditText)myView.findViewById(R.id.retrieve_email);
        tokenInput = (EditText)myView.findViewById(R.id.retrieve_token);
        passwordInput = (EditText)myView.findViewById(R.id.retrieve_password);
        retrieveMessage = (TextView)myView.findViewById(R.id.PasswordRetrieveMessage);
        retrieveProgress = (ProgressBar)myView.findViewById(R.id.PasswordRetrieveProgress);
        retrieveButton = (Button)myView.findViewById(R.id.PasswordRetrieveButton);
        retrieveButton.setOnClickListener(view -> {
            if (isInit) resetPasswordInit();
            else resetPasswordFinish();
        });
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myListener = (PasswordRetrieveActivity)context;
    }
    private void resetPasswordInit()
    {
        emailInput.setError(null);
        tokenInput.setError(null);
        passwordInput.setError(null);
        retrieveMessage.setText(null);
        myEmail = emailInput.getText().toString();
        int err = 0;
        if(!NewLoginValidation.validateEmail(myEmail))
        {
            err++;
            emailInput.setError(getString(R.string.email_must_be_valid));
        }
        if(err ==0)
        {
            retrieveProgress.setVisibility(View.VISIBLE);
            subscription.add(NetworkUtil.getRetrofit().resetPasswordInit(myEmail)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError));
        }
    }

    private void resetPasswordFinish()
    {
        emailInput.setError(null);
        tokenInput.setError(null);
        passwordInput.setError(null);
        retrieveMessage.setText(null);
        String token = tokenInput.getText().toString();
        String password = passwordInput.getText().toString();
        int err = 0;
        if(!NewLoginValidation.validateFields(token))
        {
            err++;
            tokenInput.setError(getString(R.string.token_should_not_be_empty));
        }
        if(!NewLoginValidation.validateFields(password))
        {
            err++;
            passwordInput.setError(getString(R.string.password_cant_be_empty));
        }
        if(err ==0)
        {
            retrieveProgress.setVisibility(View.VISIBLE);
            User user = new User();
            user.setPassword(password);
            user.setToken(token);
            subscription.add(NetworkUtil.getRetrofit().resetPasswordFinish(myEmail,user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse,this::handleError));
        }
    }

    private void handleResponse(Response response)
    {
        retrieveProgress.setVisibility(View.GONE);
        if(isInit)
        {
            isInit = false;
            retrieveMessage.setVisibility(View.VISIBLE);
            retrieveMessage.setText(response.getMessage());
            emailInput.setVisibility(View.GONE);
            tokenInput.setVisibility(View.VISIBLE);
            passwordInput.setVisibility(View.VISIBLE);
        }
        else
        {
            myListener.onPasswordReset(response.getMessage());
            Toast.makeText(getContext(), R.string.dismiss,Toast.LENGTH_LONG).show();
        }
    }
    private void handleError(Throwable error)
    {
        retrieveProgress.setVisibility(View.GONE);
        if(error instanceof HttpException)
        {
            Gson gson  = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                retrieveMessage.setVisibility(View.VISIBLE);
                retrieveMessage.setText(response.getMessage());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            retrieveMessage.setVisibility(View.VISIBLE);
            retrieveMessage.setText(getString(R.string.network_error));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
