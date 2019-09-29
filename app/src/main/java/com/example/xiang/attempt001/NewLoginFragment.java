package com.example.xiang.attempt001;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by xiang on 2017-06-16.
 */

public class NewLoginFragment extends Fragment {
    View myView;
    public static ProgressDialog anonProgress;
    public static Context newLoginFragmentContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Rect displayRect = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRect);
        myView = inflater.inflate(R.layout.fragment_new_login, container, false);
        newLoginFragmentContext = NewLoginFragment.this.getContext();
        anonProgress = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        TextView newLoginLogo = (TextView) myView.findViewById(R.id.newLoginLogo);
        newLoginLogo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"SourceSansPro-Bold.ttf"));
        Button newLoginSignIn = (Button)myView.findViewById(R.id.newLoginSigninButton);
        Button newLoginGuest = (Button)myView.findViewById(R.id.newLoginGuestButton);
        newLoginSignIn.setMinimumWidth((int)(displayRect.width()*0.9f));
        newLoginGuest.setMinimumWidth((int)(displayRect.width()*0.9f));
        return myView;
    }

}
