package net.ivanvega.basededatosconrooma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import net.ivanvega.basededatosconrooma.data.AppDataBase;
import net.ivanvega.basededatosconrooma.data.User;
import net.ivanvega.basededatosconrooma.data.UserDao;

import basededatosconrooma.R;


public class MainActivity extends AppCompatActivity {
    Button btnIn, btnQue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ContactsContract.Contacts.CONTENT_URI;


        btnIn = findViewById(R.id.btnInsert);
        btnQue = findViewById(R.id.btnQuery);

        btnIn.setOnClickListener(view -> {

            AppDataBase db =
                    AppDataBase.getDataBaseInstance(getApplication());

            UserDao dao = db.getUserDao();

            AppDataBase.databaseWriteExecutor.execute(() -> {
                User u = new User();
                u.uid=0;

                u.firstName = "Juan";
                u.lastName = "Peres";
                dao.insertAll(u);
                //Toast.makeText(this, "insertado", Toast.LENGTH_SHORT).show();
            });

        });

        btnQue.setOnClickListener(view -> {
            AppDataBase db =
                    AppDataBase.getDataBaseInstance(getApplication());

            UserDao dao = db.getUserDao();


            AppDataBase.databaseWriteExecutor.execute(() -> {
                for(User item : dao.getAll()){
                    Log.d("Usuario", item.uid + " " +  item.firstName
                            + " " + item.lastName);
                }
            });


        });

    }
}