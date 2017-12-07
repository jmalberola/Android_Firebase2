package com.example.juanmi.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.juanmi.firebase.model.Cancion;
import com.example.juanmi.firebase.model.Disco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText text_cancion;
    Button boton_anyadir;
    Spinner spin_grupo;

    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_cancion = (EditText) findViewById(R.id.editText);
        spin_grupo = (Spinner) findViewById(R.id.spinner);
        boton_anyadir = (Button) findViewById(R.id.button);

        bbdd = FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_discos));

        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayAdapter<String> adaptador;
                ArrayList<String> listado = new ArrayList<String>();

                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    Disco disco = datasnapshot.getValue(Disco.class);
                    String titulo = disco.getTitulo();
                    listado.add(titulo);
                }

                adaptador = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listado);
                spin_grupo.setAdapter(adaptador);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       boton_anyadir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String cancion = text_cancion.getText().toString();
                String disco = spin_grupo.getSelectedItem().toString();

                if(!TextUtils.isEmpty(cancion)){
                   Cancion c = new Cancion(cancion,disco);

                   DatabaseReference bbdd2 = FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_canciones));

                   String clave = bbdd2.push().getKey();

                   bbdd2.child(clave).setValue(c);

                   Toast.makeText(MainActivity.this, "Canción añadida", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(MainActivity.this, "Debes de introducir una canción", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
