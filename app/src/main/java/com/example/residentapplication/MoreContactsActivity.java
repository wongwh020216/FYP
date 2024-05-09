package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.residentapplication.databinding.ActivityMoreContactsBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MoreContactsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ContactAdapter contactAdapter;

    ActivityMoreContactsBinding binding;
    String contactName, addContact;
    FirebaseDatabase db;
    DatabaseReference ref;


    private Drawable deleteIcon;
    private ColorDrawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_contacts);

        binding = ActivityMoreContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("OtherEmergencyContacts"), Contacts.class)
                        .build();

        contactAdapter = new ContactAdapter(options);
        recyclerView.setAdapter(contactAdapter);

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.delete);
        background = new ColorDrawable(Color.RED);

        // Swipe to delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                showConfirmationDialog(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MoreContactsActivity.this,R.color.RED))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                        .create()
                        .decorate();
//                int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView
//
//
//                if (dX > 0) { // Swiping to the right
//                    background.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX + backgroundCornerOffset, itemView.getBottom());
//                } else if (dX < 0) { // Swiping to the left
//                    background.setBounds(itemView.getRight() + (int) dX - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//                } else { // view is unSwiped
//                    background.setBounds(0, 0, 0, 0);
//                }
//
//                background.draw(c);


                // Reset the view bounds after swiping
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);




    findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactName = binding.contactName.getText().toString();
                addContact = binding.addContact.getText().toString();

                if (!contactName.isEmpty() && !addContact.isEmpty()) {
                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference("OtherEmergencyContacts");

                    // Check if the contactName already exists
                    ref.child(contactName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Contact name already exists
                                Toast.makeText(MoreContactsActivity.this, "Contact name already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                // Contact name doesn't exist, add the contact
                                Contacts contacts = new Contacts(contactName, addContact);
                                ref.child(contactName).setValue(contacts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            binding.contactName.setText("");
                                            binding.addContact.setText("");
                                            Toast.makeText(MoreContactsActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MoreContactsActivity.this, "Failed to register: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                            Toast.makeText(MoreContactsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MoreContactsActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MoreContactsActivity.this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactAdapter.deleteItem(position);
                        Toast.makeText(MoreContactsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactAdapter.notifyItemChanged(position); // Notify adapter to redraw the item
                    }
                })
                .setCancelable(false) // Prevent dialog from being dismissed by tapping outside
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactAdapter.startListening();
    }
}