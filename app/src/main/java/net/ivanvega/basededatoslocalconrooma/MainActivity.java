package net.ivanvega.basededatoslocalconrooma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ivanvega.basededatoslocalconrooma.data.AppDatabase;
import net.ivanvega.basededatoslocalconrooma.data.User;
import net.ivanvega.basededatoslocalconrooma.data.UserDao;
import net.ivanvega.basededatoslocalconrooma.provider.UsuarioContrato;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    Button btnInsert, btnRead;
    EditText txtFirstName, txtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.btnInsert);
        btnRead = findViewById(R.id.btnRead);

        btnInsert.setOnClickListener(view -> {
            AppDatabase db = AppDatabase.getDatabaseInstance(getApplication());
            UserDao dao = db.userDao();

            txtFirstName = (EditText) findViewById(R.id.txtFirstName);
            txtLastName = (EditText) findViewById(R.id.txtLastName);

            if (txtFirstName.getText().toString().length() > 0 && txtLastName.getText().toString().length() > 0) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    User user = new User();
                    user.firstName = txtFirstName.getText().toString();
                    user.lastName = txtLastName.getText().toString();
                    dao.insertAll(user);
                });

                Toast.makeText(this, "Insertado", Toast.LENGTH_LONG).show();
                txtFirstName.setText(null);
                txtLastName.setText(null);
            } else {
                if (txtFirstName.getText().toString().length() < 1) {
                    Toast.makeText(this, "Ingresa el nombre", Toast.LENGTH_SHORT).show();
                }
                if (txtLastName.getText().toString().length() < 1) {
                    Toast.makeText(this, "Ingresa el apellido", Toast.LENGTH_SHORT).show();
                }
            }


        });

        btnRead.setOnClickListener(view -> {
            AppDatabase db = AppDatabase.getDatabaseInstance(getApplication());
            UserDao dao = db.userDao();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    dao.getAll().stream().forEach(user -> {
                        Log.i("Consulta de usuarios", user.uid + " " + user.firstName + " " + user.lastName);
                    });
                });
            } else {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    for (User user : dao.getAll()) {
                        Log.d("Consulta de usuarios", user.firstName + " " + user.lastName);
                    }
                });
            }
            getUser("Juan Lara");
        });

    }

    private void getUser(String value) {
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(UsuarioContrato.CONTENT_URI, value), UsuarioContrato.COLUMNS_NAME, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("USER", cursor.getInt(0) + " - " + cursor.getString(1));
            }
        }
    }
}