
package com.example.among.children;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.among.R;
import com.example.among.children.fragment.chatFragment;
import com.example.among.children.fragment.communityFragment;
import com.example.among.children.fragment.homeFragment;
import com.example.among.children.fragment.scheduleFragment;
import com.example.among.children.map.familyChat.MainActivity;
import com.example.among.children.user.ProfileActivity;
import com.example.among.function.PolicyFragment;
import com.example.among.testLogin.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class childrenActivity extends AppCompatActivity {
    //승인받을 권한의 목록
    String[] permission_list = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private ArrayList<String> datalist = new ArrayList<String>();

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        runPermission();
        //앱바
        toolbar=findViewById(R.id.toolbarChild);
        toolbar.setTitle("AMONG");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //바텀 네비
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.navigation_home);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null); 이게 쌓이는거
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home :
                            openFragment(homeFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_wellfare:
                            openFragment(PolicyFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_chat:
                            openFragment(chatFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_community:
                            openFragment(communityFragment.newInstance("", ""));
                            return true;
                        case R.id.navigation_question:
                            openFragment(scheduleFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };
    //FloatingActionButton을 눌렀을 때 홈화면이 보이도록 구현
    public void home_btn(View v){
        openFragment(homeFragment.newInstance("", ""));
    }

    //권한을 체크할 메서드 : 승인처리
    public void runPermission(){
        //하위버전이면 실행되지 않도록 처리
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return; // 이러면 그냥 종료
        }
        //모든 권한을 셀프체크  permission_list에서 하나씩 꺼내서 작업
        for(String permission:permission_list){
            int chk = checkCallingOrSelfPermission(permission);
            //권한 셀프 체크가 안되는 경우에
            if(chk== PackageManager.PERMISSION_DENIED){
                requestPermissions(permission_list,0);
                break;
            }

        }
    }
    //앱바 로그아웃
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_child_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sign_out:{
                signOut();
                return true;
            }
            case R.id.action_familyRoom:{
                //프로필 클릭 시 전화, 영통, 닫기, 메세지
                popup();
                return true;
            }
            case R.id.action_profile:{
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }

    }

    public void popup(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(childrenActivity.this);
        //AlertDialog의 타이틀을 정의
        builder.setTitle("데이터입력");

        //AlertDialog에 보여질 화면을 inflate
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.input, null);//뷰를 따로 만들어 작업

        //AlertDialog에 추가할 버튼을 정의
        builder.setPositiveButton("확인", new DialogListener());
        builder.setNegativeButton("취소",new DialogListener());
        //AlertDialog에 화면 설정. 붙이기
        builder.setView(dialogView);
        builder.show();
    }
    class DialogListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

            AlertDialog inputAlert = (AlertDialog)dialog;
            EditText input = inputAlert.findViewById(R.id.input);
            String data = input.getText().toString();

            Intent intent = new Intent(childrenActivity.this, MainActivity.class);

            //intent에 공유할 데이터 저장
            intent.putExtra("familyRoom",
                    data);
            startActivity(intent);


        }
    }

}




