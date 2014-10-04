package com.prototype.shoutout.shoutoutprototype;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.util.List;

//Azure Services
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

//Authentication with Google and Microsoft is available.
public class Authenticate extends Activity {

    //Table representation of the shouts.
    private MobileServiceTable<Shouts> mShoutsTable;

    //Table view of the shout from the DB
    private ShoutsView mView;

    private EditText mNewShout;

    //Progress Spinner
    private ProgressBar mProgressBar;

    private MobileServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        try {
            //Azure Client
            mClient = new MobileServiceClient(
                    "https://shoutoutazure.azure-mobile.net/",
                    "mIVcmiDjoRmUuuZKMATuCzWbDdHIdy74",
                    this
            );

            //TODO Run these functions in response to a button
            //Google Authentication
            //authenticateGoogle();
            //Microsoft Authentication
            authenticateMicrosoft();


        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }
    }

    //Some basic helpers to test the view and server response:

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**
     * Select an option from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            refreshItemsFromTable();
        }

        return true;
    }

    /**
     * Mark an item as completed
     *
     * @param shout
     *            The item to mark
     */
    public void checkItem(Shouts shout) {
        if (mClient == null) {
            return;
        }

        mShoutsTable.update(shout, new TableOperationCallback<Shouts>() {

            public void onCompleted(Shouts entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {

                    mView.remove(entity);

                } else {
                    createAndShowDialog(exception, "Error");
                }
            }

        });
    }

    //Add a new shout
    public void addItem(View view) {
        if (mClient == null) {
            return;
        }

        // Create a new shout item
        Shouts shouts = new Shouts();

        shouts.setShout(mNewShout.getText().toString());

        // Insert the new item
        mShoutsTable.insert(shouts, new TableOperationCallback<Shouts>() {

            public void onCompleted(Shouts entity, Exception exception, ServiceFilterResponse response) {

                if (exception == null) {

                    mView.add(entity);

                } else {
                    createAndShowDialog(exception, "Error");
                }

            }
        });

        mNewShout.setText("");
    }

    /**
     * Refresh the list with the items in the Mobile Service Table
     */
    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        mShoutsTable.where().field("complete").eq(val(false)).execute(new TableQueryCallback<Shouts>() {

            public void onCompleted(List<Shouts> result, int count, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    mView.clear();

                    for (Shouts shout : result) {
                        mView.add(shout);
                    }

                } else {
                    createAndShowDialog(exception, "Error");
                }
            }
        });
    }

    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
                                  final ServiceFilterResponseCallback responseCallback) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {

                @Override
                public void onResponse(ServiceFilterResponse response, Exception exception) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    if (responseCallback != null)  responseCallback.onResponse(response, exception);
                }
            });
        }
    }

    private void createTable() {

        // Get the Mobile Service Table instance to use
        mShoutsTable = mClient.getTable(Shouts.class);

        mNewShout = (EditText) findViewById(R.id.textNewToDo);

        // Create an adapter to bind the items with the view
        mView = new ShoutsView(this, R.layout.row_list_shouts);
        ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(mView);

        // Load the items from the Mobile Service
        refreshItemsFromTable();
    }

    private void authenticateGoogle() {

        // Login using the Google provider.
        mClient.login(MobileServiceAuthenticationProvider.Google,
                new UserAuthenticationCallback() {

                    @Override
                    public void onCompleted(MobileServiceUser user,
                                            Exception exception, ServiceFilterResponse response) {

                        if (exception == null) {
                            createAndShowDialog(String.format(
                                    "You are now logged in - %1$2s",
                                    user.getUserId()), "Success");
                            createTable();
                        } else {
                            createAndShowDialog("You must log in. Login Required", "Error");
                        }
                    }
                });
    }

    private void authenticateMicrosoft() {

        // Login using the Microsoft provider.
        mClient.login(MobileServiceAuthenticationProvider.MicrosoftAccount,
                new UserAuthenticationCallback() {

                    @Override
                    public void onCompleted(MobileServiceUser user,
                                            Exception exception, ServiceFilterResponse response) {

                        if (exception == null) {
                            createAndShowDialog(String.format(
                                    "You are now logged in - %1$2s",
                                    user.getUserId()), "Success");
                            createTable();
                        } else {
                            createAndShowDialog("You must log in. Login Required", "Error");
                        }
                    }
                });
    }
}


