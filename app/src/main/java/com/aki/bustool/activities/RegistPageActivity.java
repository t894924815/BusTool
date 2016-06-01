package com.aki.bustool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aki.bustool.R;
import com.aki.bustool.utils.Initialize;
import com.aki.bustool.utils.UserDao;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistPageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.back_ico)
    ImageButton backIco;
    @BindView(R.id.confirm_regist)
    Button confirmRegist;
    @BindView(R.id.edge)
    TextView edge;
    @BindView(R.id.back_head)
    TextView backHead;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.pass_confirm)
    EditText passConfirm;
    private boolean userFull = false;
    private boolean passFull = false;
    private boolean confirmPassFull = false;
    private boolean nickNameFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_page);
        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        backIco.setOnClickListener(this);
        userName.addTextChangedListener(new OnGetUser(true,false));
        password.addTextChangedListener(new OnGetUser(false,false));
        passConfirm.addTextChangedListener(new OnGetUser(false,true));

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back_ico:
                break;
            case R.id.confirm_regist:
                regist();
                break;
        }
    }

    private void regist() {
        if(null != userName && null != pass && null != confirmPass){
            if(pass.equals(confirmPass)){
                registData();
            }else{
                Toast.makeText(this,"两次密码输入不一致!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registData() {
        UserDao registDao = new UserDao(this,userId,pass,nickName);
        registDao.registUser();
        Toast.makeText(this,"注册成功!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("UserId",userId);
        intent.putExtra("PassWord",pass);
        setResult(Initialize.REGIST_RESULT,intent);
        this.finish();
    }


    private String userId;
    private String pass;
    private String confirmPass;
    private String nickName;

    class OnGetUser implements TextWatcher {

        private boolean isUser = false;
        private boolean isConfirmPass = false;

        public OnGetUser(boolean isUser,boolean isConfirmPass) {
            this.isUser = isUser;
            this.isConfirmPass = isConfirmPass;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(isSpace(editable.toString())){
                if(isUser){
                    userFull = false;
                }else if(isConfirmPass){
                    confirmPassFull = false;
                }else{
                    passFull = false;
                }
            }else{
                if(isUser){
                    userFull = true;
                    userId = editable.toString();
                }else if(isConfirmPass){
                    confirmPassFull = true;
                    confirmPass = editable.toString();
                }else{
                    passFull = true;
                    pass = editable.toString();
                }
            }
            if(userFull && passFull && confirmPassFull){
                confirmRegist.setEnabled(true);
            }else{
                confirmRegist.setEnabled(false);
            }
        }
    }

    public boolean isSpace(String text){
        boolean i = false;
        for(char a:text.toCharArray()){
            if(' ' == a){
                i = true;
            }
        }
        if(i){
            return true;
        }else{
            return false;
        }
    }
}
