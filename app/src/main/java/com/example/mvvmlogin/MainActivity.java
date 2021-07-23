package com.example.mvvmlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mvvmlogin.databinding.ActivityMainBinding;
import com.na_at.sdk.commons.config.FadConfig;
import com.na_at.sdk.commons.config.FadCredentials;
import com.na_at.sdk.commons.config.module.FaceConfig;
import com.na_at.sdk.commons.model.dataaccess.DataAccessCallback;
import com.na_at.sdk.commons.model.dataaccess.FadProcess;
import com.na_at.sdk.commons.util.StringUtils;
import com.na_at.sdk.manager.FadManager;

public class MainActivity extends AppCompatActivity {

    private FadManager mFadManager;
    private static final int FAD_SDK_REQUEST = 200;
    ActivityMainBinding binding;
    private ActivityViewModel mainViewModel;
    String mUser;
    String mPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup default credentials
        mFadManager = FadManager.builder(this)
                .build();

        mFadManager.getFadProcess("144efee9-cbcd-422e-9204-63c091d9aaba", new DataAccessCallback.GetFadProcess() {
            @Override
            public void onGetFadProcess(FadProcess process) {
                process.getProcessId();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.setUser(binding.etuser.getText().toString().trim());
                mainViewModel.setPass(binding.etpass.getText().toString().trim());
                if(mainViewModel.valid()){
                    confirm();
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario / Password incorrecto",Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Observer <String> observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etuser.setText(s);
            }
        };
        final Observer <String> observerPass = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etpass.setText(s);
            }
        };
        mainViewModel.getUser().observe(this,observer);
        mainViewModel.getPass().observe(this,observerPass);
    }

    public void confirm(){
        mUser = mainViewModel.getUser().getValue();
        mPass = mainViewModel.getPass().getValue();

        FadCredentials credentials = FadCredentials.builder()
                .client("fad")
                .secret("fadsecret")
                .username(mUser)
                .password(mPass)
                .build();

        //main config
        FadConfig.Builder builderConfig = FadConfig.builder()
                .endpoint(StringUtils.encode("https://uat.firmaautografa.com"))
                .setOnlineProcess(true)
                .requestLocation(true)
                .enablePortraitMode()
                .preventScreenCapture(false)
                .credentials(credentials);

        builderConfig.addConfig(faceConfig());

        FadManager.IntentBuilder intentBuilder = mFadManager.newIntentBuilder()
                .showHeader(true)
                .showSubHeader(false)
                .config(builderConfig.build());



        startActivityForResult(intentBuilder.build(this), FAD_SDK_REQUEST);
    }


    private FaceConfig faceConfig() {
        int[] gestures = new int[]{
                FaceConfig.GESTURE_TURN_RIGHT,
                FaceConfig.GESTURE_TURN_LEFT,
                FaceConfig.GESTURE_BLINK,
                FaceConfig.GESTURE_SMILE,
        };
        FaceConfig faceConfig = FaceConfig.builder()
                .mode(FaceConfig.MODE_DYNAMIC)
                .availableGestures(gestures) //Pasamos el arreglo con los gestos.
                .onlyFrontCamera(true)//Camara frontal activada.
                .onlyRearCamera(false)//Camara Trasera de deshabilitada.
                .build();

        return faceConfig;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUser = savedInstanceState.getString("user");
        mPass = savedInstanceState.getString("pass");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user", mainViewModel.getUser().getValue());
        outState.putString("pass", mainViewModel.getPass().getValue());
    }

}