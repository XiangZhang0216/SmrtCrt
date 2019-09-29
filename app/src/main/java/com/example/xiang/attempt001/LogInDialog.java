package com.example.xiang.attempt001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import org.json.JSONObject;

/**
 * Created by xiang on 2017-06-18.
 */

public class LogInDialog extends DialogFragment{
    View myView;
    private Button Facebook;
    private Button Google;
    private Button Twitter;
    CallbackManager facebookCallbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private EditText emailInput;
    private EditText passwordInput;
    private Button logInButton;
    private ProgressBar logInProgress;
    private TextView forgotPassword;
    private TextView createAccount;
    private CompositeSubscription subscription;
    private CompositeSubscription productSubscription;
    private SharedPreferences sharedPreferences;
    private SharedPreferences loginInfoSharedPreferences;
    public static final String TAG = LogInDialog.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.dialog_log_in,container,false);
        subscription = new CompositeSubscription();
        Twitter = (Button)myView.findViewById(R.id.LogInTwitter);
        Google = (Button)myView.findViewById(R.id.LogInGoogle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getContext());

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };


        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Profile profile = Profile.getCurrentProfile();
                //Goto next activity with profile
                Intent intent = new Intent (getActivity(), HomeScreen.class);
                boolean guest = false;

                //Getting Email
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v(TAG,response.toString());

                            }
                        }
                );
                intent.putExtra("FromAnon",guest);
                intent.putExtra("FromFacebook",true);
                intent.putExtra("FacebookName",profile.getFirstName());
                intent.putExtra("FacebookSurn",profile.getLastName());
                intent.putExtra("FacebookURL",profile.getProfilePictureUri(150,150).toString());
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };


        emailLayout=(TextInputLayout)myView.findViewById(R.id.ti_login_email);
        passwordLayout=(TextInputLayout)myView.findViewById(R.id.ti_login_password);
        emailInput=(EditText)myView.findViewById(R.id.login_email);
        passwordInput=(EditText)myView.findViewById(R.id.login_password);
        logInButton=(Button)myView.findViewById(R.id.LogInButton);
        logInButton.setOnClickListener(view -> logIn());
        forgotPassword=(TextView)myView.findViewById(R.id.ForgotPassword);
        createAccount = (TextView)myView.findViewById(R.id.CreateAccount);
        createAccount.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                if(activeNetwork != null && activeNetwork.isConnected()) {

                    CreateAccountDialog newDialogue = new CreateAccountDialog();
                    FragmentManager newDialogueFM = getActivity().getSupportFragmentManager();
                    newDialogue.show(newDialogueFM, CreateAccountDialog.TAG);
                    dismiss();

                }
                else
                {
                    Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        forgotPassword.setOnClickListener(View -> passwordRetrieve());
        logInProgress=(ProgressBar)myView.findViewById(R.id.logInProgress);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void logIn()
    {
        emailInput.setError(null);
        passwordInput.setError(null);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        int err = 0;
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
            loginInfoSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor loginInfoEditor = loginInfoSharedPreferences.edit();
            loginInfoEditor.putString("PreferredEmail", email);
            loginInfoEditor.putString("PreferredPassword",password);
            loginInfoEditor.apply();
            logInProgress.setVisibility(View.VISIBLE);
            subscription.add(NetworkUtil.getRetrofit(email,password).login()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError));
        }
        else
        {
            if(getView()!=null)
            {
                Snackbar.make(getView(), R.string.enter_valid_details, Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    private void handleResponse(Response response)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NewLoginConstants.TOKEN,response.getToken());
        editor.putString(NewLoginConstants.EMAIL,response.getMessage());
        editor.apply();

        subscription.add(NetworkUtil.getRetrofit(response.getToken()).getProfile(response.getMessage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleProfileResponse, this::handleError));

    }
    private void handleError(Throwable error)
    {
        logInProgress.setVisibility(View.GONE);
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
        logInProgress.setVisibility(View.GONE);
        startActivity(intent);
        this.getActivity().finish();

    }

    private void passwordRetrieve()
    {
        Intent intent = new Intent(getActivity(), PasswordRetrieveActivity.class);
        startActivity(intent);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    public void onStop()
    {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
