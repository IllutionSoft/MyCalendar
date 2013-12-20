package dk.illution.mycalendar;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Button;

public class Settings extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Some action");
            setListFooter(button);
        }
    }
}