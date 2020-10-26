package com.icycoke.android.medication_reminder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.icycoke.android.medication_reminder.R;

public class EventFragment extends Fragment {

    private static final String TAG = EventFragment.class.getSimpleName();

    private EditText eventTitleEditText;
    private EditText eventLocationEditText;
    private EditText eventDescriptionEditText;
    // private EditText eventStartTimeEditText;
    // private EditText eventEndTimeEditText;

    private Button addEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventTitleEditText = getActivity().findViewById(R.id.eventTitleEditText);
        eventLocationEditText = getActivity().findViewById(R.id.eventLocationEditText);
        eventDescriptionEditText = getActivity().findViewById(R.id.eventDescriptionEditText);
        // eventStartTimeEditText = getActivity().findViewById(R.id.eventStartTimeEditText);
        // eventEndTimeEditText = getActivity().findViewById(R.id.eventEndTimeEditText);

        addEventButton = getActivity().findViewById(R.id.buttonAddEvent);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventTitleEditText.getText().toString().isEmpty()
                        || eventLocationEditText.getText().toString().isEmpty()
                        || eventDescriptionEditText.getText().toString().isEmpty()
                        /*|| eventStartTimeEditText.getText().toString().isEmpty()
                        || eventEndTimeEditText.getText().toString().isEmpty()*/) {
                    Toast.makeText(
                            getActivity(),
                            getResources().getString(R.string.event_field_empty_alert),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, eventTitleEditText.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocationEditText.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDescriptionEditText.getText().toString());

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        Log.d(TAG, "onClick: starting to insert event");
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "onClick: there is no app that can support the event insert action");
                    }
                }
            }
        });
    }
}
