package com.bangjoni.agentpulsa;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bangjoni.agentpulsa.db.DBHelper;
import com.bangjoni.agentpulsa.util.Constants;
import com.bangjoni.agentpulsa.util.WebRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DetailOrderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView txtOrderCode, txtOrderDetail, txtCustName, txtCustPhone, txtCustAddress;
    Button btnRoute, btnAccept;
    GoogleApiClient mGoogleApiClient;
    Location driverLocation;
    Location custLocation;
    String orderId;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        db = new DBHelper(this);
        txtOrderCode = (TextView) findViewById(R.id.txtOrderCode);
        txtOrderDetail = (TextView) findViewById(R.id.txtOrderDetail);
        txtCustName = (TextView) findViewById(R.id.txtOrderCustName);
        txtCustPhone = (TextView) findViewById(R.id.txtOrderCustPhone);
        txtCustAddress = (TextView) findViewById(R.id.txtOrderCustAddress);

        if (TextUtils.isEmpty(db.getLoggedInID())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            setupGoogleAPIClient();
        }
    }

    private void setupButton() {
        btnRoute = (Button) findViewById(R.id.btnRoute);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+driverLocation.getLatitude()+","+driverLocation.getLongitude()
                                +"&daddr="+custLocation.getLatitude()+","+custLocation.getLongitude()));
                startActivity(intent);
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AcceptOrderTask().execute(orderId);
            }
        });
    }

    private void setupGoogleAPIClient () {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private class LoadOrderTask extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailOrderActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject reqObj = new JSONObject();
                reqObj.put("order_id", params[0]);

                String responseStr = WebRequest.makeJSONPostCall(Constants.BASE_URL + "getorder.php", reqObj);
                JSONObject respObj = new JSONObject(responseStr);

                if (respObj.has("status")) {
                    if (TextUtils.equals(respObj.getString("status"), "200")) {
                        JSONObject merchantData = respObj.getJSONObject("data");
                        return merchantData;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                orderId = jsonObject.getString("order_id");
                txtOrderCode.setText(jsonObject.getString("order_code"));
                txtOrderDetail.setText(jsonObject.getString("order_detail"));
                txtCustName.setText(jsonObject.getString("customer_name"));
                txtCustPhone.setText(jsonObject.getString("contact_no"));
                txtCustAddress.setText(jsonObject.getString("customer_address"));

                custLocation = new Location(mGoogleApiClient.toString());
                custLocation.setLatitude(jsonObject.getDouble("customer_location_lat"));
                custLocation.setLongitude(jsonObject.getDouble("customer_location_long"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancelAll();

            setupButton();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        driverLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        new LoadOrderTask().execute(getIntent().getStringExtra("orderId"));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class AcceptOrderTask extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailOrderActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject reqObj = new JSONObject();
                reqObj.put("order_id", params[0]);
                reqObj.put("merchant_id", db.getLoggedInID());

                String responseStr = WebRequest.makeJSONPostCall(Constants.BASE_URL + "acceptorder.php", reqObj);
                JSONObject respObj = new JSONObject(responseStr);
                return respObj;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr="+driverLocation.getLatitude()+","+driverLocation.getLongitude()
                            +"&daddr="+custLocation.getLatitude()+","+custLocation.getLongitude()));
            startActivity(intent);
        }
    }

}
