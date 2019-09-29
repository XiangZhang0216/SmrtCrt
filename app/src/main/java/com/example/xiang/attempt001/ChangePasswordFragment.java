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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by xiang on 2017-06-27.
 */

public class ChangePasswordFragment extends Fragment{
    View myView;
    public interface Listener
    {
        void onPasswordChanged();
    }
    public static final String TAG = ChangePasswordFragment.class.getSimpleName();
    private TextInputLayout oldLayout;
    private TextInputLayout newLayout;
    private EditText oldInput;
    private EditText newInput;
    private Button changeButton;
    private Button cancelButton;
    public static TextView changeMessage;
    private ProgressBar changeProgress;

    private CompositeSubscription subscription;
    private String myToken;
    private String myEmail;

    private Listener myListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_change_password,container,false);
        subscription = new CompositeSubscription();
        Bundle bundle = getArguments();
        myToken = bundle.getString(NewLoginConstants.TOKEN);
        myEmail = bundle.getString(NewLoginConstants.EMAIL);
        oldLayout = (TextInputLayout)myView.findViewById(R.id.ti_old_password);
        newLayout = (TextInputLayout)myView.findViewById(R.id.ti_new_password);
        oldInput = (EditText)myView.findViewById(R.id.et_old_password);
        newInput = (EditText)myView.findViewById(R.id.et_new_password);
        changeMessage = (TextView)myView.findViewById(R.id.tv_message);
        changeButton = (Button)myView.findViewById(R.id.btn_change_password);
        cancelButton = (Button)myView.findViewById(R.id.btn_cancel);
        changeProgress = (ProgressBar)myView.findViewById(R.id.change_password_progress);
        changeButton.setOnClickListener(view -> changePassword());
        cancelButton.setOnClickListener(view -> cancelChangePassword());
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myListener = (ChangePassword)context;
    }
    private void changePassword()
    {
        oldInput.setError(null);
        newInput.setError(null);
        String oldPassword = oldInput.getText().toString();
        String newPassword = newInput.getText().toString();
        int err = 0;
        if(!NewLoginValidation.validateFields(oldPassword))
        {
            err++;
            oldLayout.setError(getString(R.string.password_cant_be_empty));
        }
        if(!NewLoginValidation.validateFields(newPassword))
        {
            err++;
            newLayout.setError(getString(R.string.password_cant_be_empty));
        }
        if(err == 0)
        {
            User user = new User();
            user.setPassword(oldPassword);
            user.setNewPassword(newPassword);
            subscription.add(NetworkUtil.getRetrofit(myToken).changePassword(myEmail,user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError));
            changeProgress.setVisibility(View.VISIBLE);
        }
    }
    private void handleResponse(Response response)
    {
        changeProgress.setVisibility(View.GONE);
        myListener.onPasswordChanged();
    }
    private void handleError(Throwable error)
    {
        changeProgress.setVisibility(View.GONE);
        if(error instanceof HttpException)
        {
            Gson gson = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                changeMessage.setVisibility(View.VISIBLE);
                if (response.getMessage().contains("Invalid")) {
                    changeMessage.setText(R.string.invalid_old_password);
                }
                else
                {
                    changeMessage.setText(R.string.internal_server_error);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            changeMessage.setVisibility(View.VISIBLE);
            changeMessage.setText(R.string.network_error);
        }
    }
    private void cancelChangePassword()
    {
        getActivity().onBackPressed();
    }

}
