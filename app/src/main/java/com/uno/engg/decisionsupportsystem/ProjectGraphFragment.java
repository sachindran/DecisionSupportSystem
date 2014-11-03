package com.uno.engg.decisionsupportsystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.telerik.widget.chart.engine.databinding.PropertyNameDataPointBinding;
import com.telerik.widget.chart.visualization.cartesianChart.RadCartesianChartView;
import com.telerik.widget.chart.visualization.cartesianChart.axes.CategoricalAxis;
import com.telerik.widget.chart.visualization.cartesianChart.axes.LinearAxis;
import com.telerik.widget.chart.visualization.cartesianChart.series.categorical.LineSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectGraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int position;
    private String ProjectTitle;
    Bundle args1;
    private Button btnFromDate;
    private Button btnToDate;
    private static GregorianCalendar FromDate;
    private static GregorianCalendar ToDate;
    private static GregorianCalendar date;
    private static int fromFlag;
    private static View myInflatedView;
    private List<MonthResult> monthResults;
    private List<MonthResult> monthResults1;
    private static int datesSet = 0;
    int[] varValues = new int[0];
    int[] varMonth = new int[0];

    private void initData() {
        monthResults = new ArrayList<MonthResult>();
        monthResults.add(new MonthResult("Jan", 12));
        monthResults.add(new MonthResult("Feb", 5));
        monthResults.add(new MonthResult("Mar", 10));
        monthResults.add(new MonthResult("Apr", 7));
    }
    private void initData1() {
        monthResults1 = new ArrayList<MonthResult>();
        monthResults1.add(new MonthResult("Jan", 1));
        monthResults1.add(new MonthResult("Feb", 4));
        monthResults1.add(new MonthResult("Mar", 7));
        monthResults1.add(new MonthResult("Apr", 5));
    }

    public class MonthResult {
        private String month;
        private double result;

        public MonthResult(String month, double result) {
            this.setMonth(month);
            this.setResult(result);
        }

        public double getResult() {
            return this.result;
        }

        public void setResult(double value) {
            this.result = value;
        }

        public String getMonth() {
            return this.month;
        }

        public void setMonth(String value) {
            this.month = value;
        }
    }

    public void plotGraph()
    {
        initData();

        LineSeries lineSeries = new LineSeries();
        lineSeries.setCategoryBinding(new PropertyNameDataPointBinding("Month"));
        lineSeries.setValueBinding(new PropertyNameDataPointBinding("Result"));
        lineSeries.setData(this.monthResults);

        initData1();
        LineSeries lineSeries1 = new LineSeries();
        lineSeries1.setCategoryBinding(new PropertyNameDataPointBinding("Month"));
        lineSeries1.setValueBinding(new PropertyNameDataPointBinding("Result"));
        lineSeries1.setData(this.monthResults1);

        RadCartesianChartView chartView = new RadCartesianChartView(myInflatedView.getContext());
        chartView.getSeries().add(lineSeries);
        chartView.getSeries().add(lineSeries1);
        CategoricalAxis horizontalAxis = new CategoricalAxis();
        chartView.setHorizontalAxis(horizontalAxis);

        LinearAxis verticalAxis = new LinearAxis();
        chartView.setVerticalAxis(verticalAxis);

        ViewGroup rootView = (ViewGroup)myInflatedView.findViewById(R.id.graphContainer);
        rootView.addView(chartView);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectGraphFragment newInstance(String param1, String param2) {
        ProjectGraphFragment fragment = new ProjectGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProjectGraphFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myInflatedView = inflater.inflate(R.layout.fragment_project_graph, container, false);
        args1 = getArguments();
        position = args1.getInt("position");
        ProjectTitle = args1.getString("ProjectTitle");

        onButtonPressed(ProjectTitle);
        btnFromDate = (Button)myInflatedView.findViewById(R.id.fromDate);
        btnToDate = (Button)myInflatedView.findViewById(R.id.toDate);
        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromFlag = 1;
                showDatePickerDialog(v);
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromFlag = 0;
                showDatePickerDialog(v);
            }
        });
        plotGraph();
        Spinner spinner = (Spinner)myInflatedView.findViewById(R.id.projects_spinner);
        ArrayList<String> a = new ArrayList<String>();
        a.add(0,"Sales");
        a.add(1,"Profit");
        a.add(2,"TurnOver");
        ArrayAdapter adapter = new ArrayAdapter(myInflatedView.getContext(),android.R.layout.simple_spinner_item,a);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataCollect();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnGraphGenerate = (Button)myInflatedView.findViewById(R.id.btn_graph_generate);
        btnGraphGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCollect();
            }
        });
        return myInflatedView;
    }
    public void dataCollect()
    {
        if(datesSet == 1)
        {
            Spinner spinnerVar = (Spinner)myInflatedView.findViewById(R.id.projects_spinner);
            Object obj = spinnerVar.getSelectedItem();
            if(ProjectTitle.contains("-"))
            {
                String a = ProjectTitle.substring(0,ProjectTitle.indexOf('-')-1);
                String b = ProjectTitle.substring(ProjectTitle.indexOf('-')+1,ProjectTitle.length()-1);
                a.concat(b);
                ProjectTitle = a;
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ProjectTitle);
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            int flagEx = 0;
            try {
                List<ParseObject> variables = query.find();
                varValues = new int[variables.size()];
                varMonth = new int[variables.size()];
                int i =0;
                for(i=0;i<variables.size();i++)
                {
                    Object obj1 = variables.get(i);
                    varValues[i] = (((ParseObject)obj1).getInt(obj.toString()));
                    varMonth[i] = (((ParseObject)obj1).getInt("createdAt"));
                }
            }
            catch (ParseException e)
            {
                flagEx = 1;
                Log.d("Exception", e.toString());
            }
            if(flagEx==0)
            {
                plotGraph();
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        /*if(date!= null)
        {
            setDate(from);
        }*/
    }

    public static void setDate()
    {
        String dateStr = (date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.YEAR);
        int fromDateSet = 0;
        int toDateSet = 0;
        switch (fromFlag)
        {
            case 1: {
                if (ToDate == null) {
                    FromDate = date;
                    EditText e = (EditText)myInflatedView.findViewById(R.id.fromDate_EditText);
                    e.setText(dateStr);
                    fromDateSet = 1;
                }
                else
                {
                    if(date.before(ToDate))
                    {
                        FromDate = date;
                        EditText e = (EditText)myInflatedView.findViewById(R.id.fromDate_EditText);
                        e.setText(dateStr);
                        fromDateSet = 1;
                    }
                    else
                    {
                        Toast.makeText((Activity)myInflatedView.getContext(), "Invalid From Date", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case 0:
            {
                if(FromDate == null)
                {
                    ToDate = date;
                    EditText e = (EditText)myInflatedView.findViewById(R.id.toDate_EditText);
                    e.setText(dateStr);
                    toDateSet = 1;
                }
                else
                {
                    if(date.after(FromDate))
                    {
                        ToDate = date;
                        EditText e = (EditText)myInflatedView.findViewById(R.id.toDate_EditText);
                        e.setText(dateStr);
                        toDateSet = 1;
                    }
                    else
                    {
                        Toast.makeText((Activity)myInflatedView.getContext(), "Invalid To Date", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            }
        }
        if(fromDateSet==1 && toDateSet ==1)
        {
            datesSet = 1;
        }
        else
        {
            datesSet = 0;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String ProjectTitle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(ProjectTitle);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
            mListener.onFragmentInteraction(ProjectTitle);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String ProjectTitle);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date = new GregorianCalendar(year,month,day);
            setDate();
        }
    }

}
