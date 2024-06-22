package com.example.pm2e10159;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pm2e10159.db.DbContactos;
import com.example.pm2e10159.entidades.Contactos;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 1;

    private ArrayList<String> contactList;
    private ArrayAdapter<String> adapter;
    private Button buttonUpdateContact;
    private Button buttonDeleteContact;
    private Button buttonShareContact;
    private Button buttonReturn;
    private String selectedPhoneNumber;
    private int selectedContactId = -1;
    private DbContactos dbContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewContacts = findViewById(R.id.listViewContacts);
        Button buttonCallContact = findViewById(R.id.buttonCallContact);
        buttonUpdateContact = findViewById(R.id.buttonUpdateContact);
        buttonDeleteContact = findViewById(R.id.buttonDeleteContact);
        buttonShareContact = findViewById(R.id.buttonShareContact);
        Button buttonViewImage = findViewById(R.id.buttonViewImage);
        buttonReturn = findViewById(R.id.buttonReturn);
        contactList = new ArrayList<>();
        dbContactos = new DbContactos(this);

        ArrayList<Contactos> contacts = dbContactos.visualizarContactos();
        for (Contactos contacto : contacts) {
            contactList.add(contacto.getId() + " - " + contacto.getNombre() + " - " + contacto.getTelefono() + " - " + contacto.getNota());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        listViewContacts.setAdapter(adapter);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contact = contactList.get(position);
                String[] contactDetails = contact.split(" - ");
                selectedContactId = Integer.parseInt(contactDetails[0]);
                selectedPhoneNumber = contactDetails[2];
            }
        });
        buttonCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoneNumber != null && !selectedPhoneNumber.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("¿Desea llamar a este contacto?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    makePhoneCall();
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.create().show();
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione un contacto para llamar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContactId != -1) {
                    // Implementar lógica de actualización aquí
                    // Por ejemplo, iniciar una actividad para editar el contacto
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione un contacto para actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContactId != -1) {
                    dbContactos.eliminarContacto(selectedContactId);

                    // Eliminar el contacto correcto del ArrayList
                    String contactToRemove = null;
                    for (String contact : contactList) {
                        if (contact.startsWith(String.valueOf(selectedContactId))) {
                            contactToRemove = contact;
                            break;
                        }
                    }
                    if (contactToRemove != null) {
                        contactList.remove(contactToRemove);
                        adapter.notifyDataSetChanged();
                    }

                    Toast.makeText(MainActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione un contacto para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonShareContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoneNumber != null && !selectedPhoneNumber.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Contacto: " + selectedPhoneNumber);
                    startActivity(Intent.createChooser(shareIntent, "Compartir contacto"));
                } else {
                    Toast.makeText(MainActivity.this, "Seleccione un contacto para compartir", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContactId == -1) {
                    Toast.makeText(MainActivity.this, "Seleccione un contacto para ver la imagen", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para ver la imagen del contacto
                    // Implementar según los requisitos de tu aplicación
                }
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + selectedPhoneNumber));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
