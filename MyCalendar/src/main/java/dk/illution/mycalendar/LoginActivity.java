package dk.illution.mycalendar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

class GoogleAuthTokenValidator extends AsyncTask<String, Void, String> {

    private Activity activity;
    private Context appContext;

    public GoogleAuthTokenValidator (Activity activity) {
        appContext = activity.getApplicationContext();
        this.activity = activity;
    }

    protected String doInBackground (String... params) {
        URL url;
        HttpURLConnection connection;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(appContext);

        /*try {
            String response= "";

            // Set the URL
            url=new URL(preferences.getString("preference_endpoint", null) + "/login/device/google?access_token=" + params[0]);


            // Open connection
            connection=(HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");

            // Set headers
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Send request
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.close();

            // Get stream
            Scanner inStream = new Scanner(connection.getInputStream());

            // Loop through stream and add get the response
            while(inStream.hasNextLine())
                response+=(inStream.nextLine());

            // Return the response
            return response;
        } catch (Exception e) {
            return null;
        }*/
        return "";
    }

    protected void onPostExecute(String response) {
        if (response != null) {
            /**if (MyCalken.parseUserTokenResponse(response, appContext)) {
                ComputerInfo.launchComputerList(activity);
            } else {
                Toast.makeText(appContext, this.activity.getString(R.string.login_error_google_validation), Toast.LENGTH_LONG).show();
            }*/
        } else {
            Toast.makeText(appContext, this.activity.getString(android.R.string.login_error_connection), Toast.LENGTH_LONG).show();
        }
    }
}

public class LoginActivity extends Activity {

    private static final int AUTHORIZATION_CODE = 1993;
    private static final int ACCOUNT_CODE = 1601;

    private AuthPreferences authPreferences;
    private AccountManager accountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_login);

        final Activity activity = this;

        Button signInWithGoogle = (Button) findViewById(android.R.id.sign_in_with_google);

        // Assign an OnClick Handler to the LoginButton
        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = LoginActivity.this;
                final Context context = activity.getApplicationContext();

                AccountManager am = AccountManager.get(context);
                final Account[] accounts = am.getAccountsByType("com.google");
                CharSequence [] accountNames = new CharSequence[accounts.length];


                if (accounts.length <= 0) {
                    Toast.makeText(context, LoginActivity.this.getString(android.R.string.login_error_google_no_accounts), Toast.LENGTH_LONG).show();
                    return;
                }

                for (int i = 0; i <= accounts.length - 1; i++) {
                    accountNames[i] = accounts[i].name;
                }

                //Prepare the list dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                //Set its title
                builder.setTitle("Choose an account");
                //Set the list items and assign with the click listener
                builder.setItems(accountNames, new DialogInterface.OnClickListener() {

                    // Click listener
                    public void onClick(DialogInterface dialog, int item) {
                        //
                        AccountManager manager = AccountManager.get(activity);

                        manager.getAuthToken(accounts[item], "oauth2:" + implodeString(getResources().getStringArray(android.R.array.google_scopes), ""), null, activity, new AccountManagerCallback<Bundle>() {
                            public void run(AccountManagerFuture<Bundle> future) {
                                try {
                                    // If the user has authorized your application to use the tasks API
                                    // a token is available.
                                    String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                                    Log.d("MyCalendar", token);
                                    new GoogleAuthTokenValidator (LoginActivity.this).execute(token);
                                    // Now you can use the Tasks API...
                                } catch (OperationCanceledException e) {
                                    // TODO: The user has denied you access to the API, you should handle that
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, null);

                    }

                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(android.R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(android.R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public static String implodeString(String[] data, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1]);
        return sb.toString();
    }

}
