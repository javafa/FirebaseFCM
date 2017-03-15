package com.veryworks.android.firebasefcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.google.android.gms.internal.zzt.TAG;

/*

 */
public class MyFirebaseInsIDService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // 서버를 구현한후 refreshedToken 을 서버측에 전달해줘야 한다
    }
}
