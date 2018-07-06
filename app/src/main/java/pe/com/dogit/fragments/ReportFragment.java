package pe.com.dogit.fragments;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pe.com.dogit.DOgITApp;
import pe.com.dogit.Manifest;
import pe.com.dogit.R;
import pe.com.dogit.models.Adoption;
import pe.com.dogit.models.Publication;
import pe.com.dogit.models.User;
import pe.com.dogit.network.DOgITService;
import pe.com.dogit.templates.TemplatePDF;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private TemplatePDF templatePDF;
    private Spinner monthSpinner;
    private Button pdfButton;
    private String[]headerUser={"Number","User","mail","Phone","Date","Status"};
    private String[]headerPublication={"Number","Pet","User","Date","Status"};
    private String[]headerAdoption={"Number","Pet","User","Date"};
    private List<Publication> publications;
    private List<User> users;
    private List<Adoption> adoptions;
    List<String> monthList = new ArrayList<String>();
    List<Publication> monthPublications = new ArrayList<>();
    List<User> monthUsers = new ArrayList<>();
    List<Adoption> monthAdoptions= new ArrayList<>();
    private BarChart userBarChart;
    private BarChart publicationBarChart;
    private BarChart adoptionBarChart;
    private String[] months;
    private int[] userByMonth = new int[12];
    private int[] publicationByMonth = new int[12];
    private int[] adoptionByMonth = new int[12];
    private int[] colors = new int[]{Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED};

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        pdfButton = view.findViewById(R.id.pdfButton);
        userBarChart = view.findViewById(R.id.userBarChart);
        publicationBarChart = view.findViewById(R.id.publicationBarChart);
        adoptionBarChart = view.findViewById(R.id.adoptionBarChart);

        int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(view.getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView(v);
            }
        });

        months = new DateFormatSymbols().getMonths();
        monthList.addAll(Arrays.asList(months));

        getPublications();
        getUsers();
        getAdoptions();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, monthList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);

        return view;
    }

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(textColor);
        //chart.setBackgroundColor(background);
        chart.animateY(animateY);
        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();

        for(int i=0; i<months.length;i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = months[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private void adoptionsByMonth(){
        int count;
        for(int j=1;j<13;j++) {
            count = 0;
            for(int i=0;i<adoptions.size();i++) {
                Integer month = Integer.parseInt(adoptions.get(i).getDate().substring(5,7));
                if (month == j) {
                    count++;
                }
            }
            adoptionByMonth[j-1] = count;
        }
        createCharts();
    }

    private void publicationsByMonth(){
        int count;
        for(int j=1;j<13;j++) {
            count = 0;
            for(int i=0;i<publications.size();i++) {
                Integer month = Integer.parseInt(publications.get(i).getPublicationDate().substring(5,7));
                if (month == j) {
                    count++;
                }
            }
            publicationByMonth[j-1] = count;
        }
        createCharts();
    }

    private void usersByMonth(){
        int count;
        for(int j=1;j<13;j++) {
            count = 0;
            for(int i=0;i<users.size();i++) {
                Integer month = Integer.parseInt(users.get(i).getSignupDate().substring(5,7));
                if (month == j) {
                    count++;
                }
            }
            userByMonth[j-1] = count;
        }
        createCharts();
    }

    private ArrayList<BarEntry> getBarEntriesUser() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<months.length;i++) {
            entries.add(new BarEntry(i,userByMonth[i]));
        }
        return entries;
    }

    private ArrayList<BarEntry> getBarEntriesPublication() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<months.length;i++) {
            entries.add(new BarEntry(i,publicationByMonth[i]));
        }
        return entries;
    }

    private ArrayList<BarEntry> getBarEntriesAdoption() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<months.length;i++) {
            entries.add(new BarEntry(i,adoptionByMonth[i]));
        }
        return entries;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(months));
        axis.setEnabled(false);
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
        axis.setGranularity(1);
    }

    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    public void createCharts() {
        userBarChart = (BarChart) getSameChart(userBarChart,"", Color.RED, Color.CYAN, 3000);
        userBarChart.setDrawGridBackground(true);
        userBarChart.setDrawBarShadow(true);
        userBarChart.setData(getBarDataUser());
        userBarChart.invalidate();
        axisX(userBarChart.getXAxis());
        axisLeft(userBarChart.getAxisLeft());
        axisRight(userBarChart.getAxisRight());

        publicationBarChart = (BarChart) getSameChart(publicationBarChart,"", Color.RED, Color.CYAN, 3000);
        publicationBarChart.setDrawGridBackground(true);
        publicationBarChart.setDrawBarShadow(true);
        publicationBarChart.setData(getBarDataPublication());
        publicationBarChart.invalidate();
        axisX(publicationBarChart.getXAxis());
        axisLeft(publicationBarChart.getAxisLeft());
        axisRight(publicationBarChart.getAxisRight());

        adoptionBarChart = (BarChart) getSameChart(adoptionBarChart,"", Color.RED, Color.CYAN, 3000);
        adoptionBarChart.setDrawGridBackground(true);
        adoptionBarChart.setDrawBarShadow(true);
        adoptionBarChart.setData(getBarDataAdoption());
        adoptionBarChart.invalidate();
        axisX(adoptionBarChart.getXAxis());
        axisLeft(adoptionBarChart.getAxisLeft());
        axisRight(adoptionBarChart.getAxisRight());

    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarDataUser() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntriesUser(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);
        return barData;
    }

    private BarData getBarDataPublication() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntriesPublication(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);
        return barData;
    }

    private BarData getBarDataAdoption() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntriesAdoption(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);
        return barData;
    }

    private void getPublications() {
        AndroidNetworking
                .get(DOgITService.PUBLICATION_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            publications = Publication.build(response.getJSONArray("publications"));
                            publicationsByMonth();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void getUsers() {
        AndroidNetworking
                .get(DOgITService.USER_TYPE_URL)
                .addPathParameter("type", "user")
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            users = User.build(response.getJSONArray("users"));
                            usersByMonth();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void getAdoptions() {
        AndroidNetworking
                .get(DOgITService.ADOPTION_URL)
                .addHeaders("Authorization", DOgITApp.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            adoptions = Adoption.build(response.getJSONArray("adoptions"));
                            adoptionsByMonth();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }


    public void pdfView(View view) {
        monthUsers.clear();
        monthPublications.clear();
        monthAdoptions.clear();
        templatePDF = new TemplatePDF(view.getContext());
        int year;
        int month;
        int position = Integer.parseInt(Long.toString(monthSpinner.getSelectedItemId())) + 1;
        if (users.size() >= 1) {
            for(int i = 0;i<users.size();i++) {
                year = Integer.parseInt(users.get(i).getSignupDate().substring(0, 4));
                month = Integer.parseInt(users.get(i).getSignupDate().substring(5,7));
                if (year == 2018 && month == position) {
                    monthUsers.add(users.get(i));
                }
            }
        }
        if (publications.size() >= 1) {
            for(int i = 0;i<publications.size();i++) {
                year = Integer.parseInt(publications.get(i).getPublicationDate().substring(0, 4));
                month = Integer.parseInt(publications.get(i).getPublicationDate().substring(5,7));
                if (year == 2018 && month == position) {
                    monthPublications.add(publications.get(i));
                }
            }
        }
        if (adoptions.size() >= 1) {
            for(int i = 0;i<adoptions.size();i++) {
                year = Integer.parseInt(adoptions.get(i).getDate().substring(0, 4));
                month = Integer.parseInt(adoptions.get(i).getDate().substring(5,7));
                if (year == 2018 && month == position) {
                    monthAdoptions.add(adoptions.get(i));
                }
            }
        }
        templatePDF.openDocument();
        templatePDF.addMetaData("DOgIT", "Reporte", "Grecia Armas");
        templatePDF.addTitles("DOgIT", "Reporte de " + String.valueOf(monthSpinner.getSelectedItem()), Calendar.getInstance().getTime().toString());
        templatePDF.addParagraph("Lista de Usuarios");
        templatePDF.createTable(headerUser, getUser());
        templatePDF.addParagraph("Lista de Publicaciones");
        templatePDF.createTable(headerPublication, getPublication());
        templatePDF.addParagraph("Lista de Adopciones");
        templatePDF.createTable(headerAdoption, getAdoption());
        templatePDF.closeDocument();
        templatePDF.viewPDF();
    }

    private ArrayList<String[]>getPublication(){
        ArrayList<String[]>rows = new ArrayList<>();

        for(int i = 0; i < monthPublications.size(); i++) {
            rows.add(new String[]{String.valueOf(i+1), monthPublications.get(i).getPet().getName(),
                    monthPublications.get(i).getUser().getName(), monthPublications.get(i).getPublicationDate().substring(0,10),
                        monthPublications.get(i).getStatus()});
        }
        return rows;
    }

    private ArrayList<String[]>getUser(){
        ArrayList<String[]>rows = new ArrayList<>();

        for(int i = 0; i < monthUsers.size(); i++) {
            rows.add(new String[]{String.valueOf(i+1), monthUsers.get(i).getName() + " " + monthUsers.get(i).getLastName(), monthUsers.get(i).getEmail(),
                    monthUsers.get(i).getMobilePhone(), monthUsers.get(i).getSignupDate().substring(0,10), monthUsers.get(i).getStatus()});
        }
        return rows;
    }

    private ArrayList<String[]>getAdoption(){
        ArrayList<String[]>rows = new ArrayList<>();

        for(int i = 0; i < monthAdoptions.size(); i++) {
            rows.add(new String[]{String.valueOf(i+1), monthAdoptions.get(i).getPublication().getPet().getName(),
                    monthAdoptions.get(i).getUser().getName(), monthAdoptions.get(i).getDate().substring(0,10)});
        }
        return rows;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }


}
