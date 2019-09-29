package com.example.xiang.attempt001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;


/**
 * Created by xiang on 2017-06-18.
 */

public class CreateAccountDialog extends DialogFragment {
    View myView;
    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Button signUpButton;
    private ProgressBar signUpProgress;
    private CompositeSubscription subscription;
    public static final String TAG = CreateAccountDialog.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.dialog_create_account,container,false);
        subscription = new CompositeSubscription();
        usernameLayout=(TextInputLayout)myView.findViewById(R.id.ti_name);
        emailLayout=(TextInputLayout)myView.findViewById(R.id.ti_email);
        passwordLayout=(TextInputLayout)myView.findViewById(R.id.ti_password);
        usernameInput=(EditText)myView.findViewById(R.id.createaccount_name);
        emailInput=(EditText)myView.findViewById(R.id.createaccount_email);
        passwordInput=(EditText)myView.findViewById(R.id.createaccount_password);
        signUpButton=(Button)myView.findViewById(R.id.CreateAccountButton);
        signUpButton.setOnClickListener(view -> signUp());
        signUpProgress=(ProgressBar)myView.findViewById(R.id.createAccountProgress);

        return myView;
    }
    public void signUp()
    {
        usernameInput.setError(null);
        passwordInput.setError(null);
        emailInput.setError(null);
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        int err=0;
        if(!NewLoginValidation.validateFields(username))
        {
            err++;
            usernameInput.setError(getString(R.string.username_cant_be_empty));
        }
        if(!NewLoginValidation.validateEmail(email))
        {
            err++;
            emailInput.setError(getString(R.string.email_must_be_valid));
        }
        if(!NewLoginValidation.validateFields(password))
        {
            err++;
            passwordInput.setError(getString(R.string.password_cant_be_empty));
        }
        if(err == 0)
        {
            User user = new User();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);
            signUpProgress.setVisibility(View.VISIBLE);
            subscription.add(NetworkUtil.getRetrofit().register(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError));
        }
        else
        {
            if(getView()!=null)
            {
                Snackbar.make(getView(), R.string.make_sure_the_details_are_valid,Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    public void handleResponse(Response response)
    {
        signUpProgress.setVisibility(View.GONE);
        if(getView()!=null)
        {


            if (response.getMessage().contains("Sucessfully"))
            {
                Snackbar.make(getView(),getString(R.string.user_registered_successfully),Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1800);
            }

            SharedPreferences loginInfoSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor loginInfoEditor = loginInfoSharedPreferences.edit();
            loginInfoEditor.putString("PreferredEmail", emailInput.getText().toString());
            loginInfoEditor.putString("PreferredPassword",passwordInput.getText().toString());
            loginInfoEditor.apply();
            signUpProgress.setVisibility(View.VISIBLE);
            subscription.add(NetworkUtil.getRetrofit(emailInput.getText().toString(),passwordInput.getText().toString()).login()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleLoginAfterSignupResponse, this::handleLoginAfterSignupError));

        }

    }
    public void handleError(Throwable error)
    {
        signUpProgress.setVisibility(View.GONE);
        if(error instanceof HttpException)
        {
            Gson gson = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                if(getView()!=null)
                {
                    if(response.getMessage().contains("Already"))
                    {
                        Snackbar.make(getView(), R.string.user_already_registered,Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Snackbar.make(getView(),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if(getView()!=null)
            {
                Snackbar.make(getView(),R.string.network_error,Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void handleLoginAfterSignupResponse(Response response)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NewLoginConstants.TOKEN,response.getToken());
        editor.putString(NewLoginConstants.EMAIL,response.getMessage());
        editor.apply();

        subscription.add(NetworkUtil.getRetrofit(response.getToken()).getProfile(response.getMessage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleProfileResponse, this::handleError));

    }
    private void handleLoginAfterSignupError(Throwable error)
    {
        signUpProgress.setVisibility(View.GONE);
        if(error instanceof HttpException)
        {
            Gson gson = new GsonBuilder().create();
            try
            {
                String errorBody = ((HttpException)error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                if(getView()!=null)
                {
                    if(response.getMessage().contains("User"))
                    {
                        Snackbar.make(getView(),R.string.user_not_found,Snackbar.LENGTH_SHORT).show();
                    }
                    else if(response.getMessage().contains("Invalid"))
                    {
                        Snackbar.make(getView(),R.string.invalid_credentials,Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {Snackbar.make(getView(),R.string.internal_server_error,Snackbar.LENGTH_SHORT).show();}

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if(getView()!=null)
            {
                Snackbar.make(getView(),R.string.network_error,Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    private void handleProfileResponse(User user)
    {

        SharedPreferences nameClearPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor nameClearEdit = nameClearPreferences.edit();
        nameClearEdit.remove("CurrentName");
        nameClearEdit.remove("ReturningName");
        nameClearEdit.remove("ReturningEmail");
        nameClearEdit.remove("ReturningId");
        nameClearEdit.remove("RenewedCart");
        nameClearEdit.remove("RenewedSuggestions");
        nameClearEdit.apply();
        ProductDataSource.returningUsername = null;
        ProductDataSource.returningUserEmail = null;
        ProductDataSource.returningUserId = null;

        ProductDataSource.returningUsername = null;
        ProductDataSource.returningUserEmail = null;
        ProductDataSource.returningUserId=null;
        ProductDataSource.returningUsername = user.getName();
        ProductDataSource.returningUserEmail = user.getEmail();
        ProductDataSource.returningUserId=user.getId();
        ProductDataSource.returningUserRenewedCart=user.getCartstring();
        ProductDataSource.returningUserSuggestions=user.getSuggestions();
        SharedPreferences returningNamePreference = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor nameEdit = returningNamePreference.edit();
        nameEdit.remove("ReturningName");
        nameEdit.remove("ReturningEmail");
        nameEdit.remove("ReturningId");
        nameEdit.remove("RenewedCart");
        nameEdit.remove("RenewedSuggestions");
        nameEdit.putString("ReturningName", user.getName());
        nameEdit.putString("ReturningEmail", user.getEmail());
        nameEdit.putString("ReturningId",user.getId());
        nameEdit.putString("RenewedCart",user.getCartstring());
        nameEdit.putString("RenewedSuggestions",user.getSuggestions());
        nameEdit.apply();

        emailInput.setText(null);
        passwordInput.setText(null);
        Intent intent = new Intent (getActivity(), HomeScreen.class);
        boolean guest = false;
        intent.putExtra("FromAnon",guest);
        intent.putExtra("FromFacebook",false);
        intent.putExtra("ProductsfromLogin",ProductDataSource.storedArray);
        intent.putExtra("ProductArrayLength",ProductDataSource.storedArray.length);
        signUpProgress.setVisibility(View.GONE);
        dismiss();
        startActivity(intent);
        this.getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
