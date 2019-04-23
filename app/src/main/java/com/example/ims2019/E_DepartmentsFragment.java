package com.example.ims2019;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class E_DepartmentsFragment extends Fragment {
//    TextView police_dept, fc_dept, trauma_ceter, fire_brigade, bomb_squad, hospital;

    ListView departments_list;
    String departments_title[] = {"Police Department", "FC Department", "Trauma Center", "Fire Brigade", "Bomb Squad", "Hospital"};
    String departments_number[] = {"03333652148", "03333652148", "03333652148", "03333652148", "03333652148", "03333652148"};
    int call_logo[] = {R.drawable.ic_call, R.drawable.ic_call, R.drawable.ic_call, R.drawable.ic_call, R.drawable.ic_call, R.drawable.ic_call};
    public E_DepartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_e__departments, container, false);

        departments_list = (ListView) v.findViewById(R.id.e_departments_list);

        //MyAdapter adapter = new MyAdapter(this, departments_title, call_logo, departments_number);
        MyAdapter myAdapter = new MyAdapter(getContext(), departments_title, call_logo, departments_number);

        departments_list.setAdapter(myAdapter);



        departments_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03331332684"));
                    startActivity(intent);
                }

                if (position == 1)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03361343918"));
                    startActivity(intent);
                }

                if (position == 2)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03361343918"));
                    startActivity(intent);
                }

                if (position == 3)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03361343918"));
                    startActivity(intent);
                }

                if (position == 4)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03361343918"));
                    startActivity(intent);
                }

                if (position == 5)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 03361343918"));
                    startActivity(intent);
                }
            }
        });

        return v;

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String eDepartments_title[];
        String eDpartments_number[];
        int imgCallLogo[];

        MyAdapter(Context c, String[] departments_title, int[] call_Logo, String[] departments_number)
        {
            super(c, R.layout.department_numbers, R.id.departments_title, departments_title);
            this.context = c;
            this.imgCallLogo = call_logo;
            this.eDepartments_title = departments_title;
            this.eDpartments_number = eDpartments_number;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View departments = layoutInflater.inflate(R.layout.department_numbers, parent, false);

            ImageView call_images = departments.findViewById(R.id.call_logo);

            TextView department_titles = departments.findViewById(R.id.departments_title);

            TextView department_number = departments.findViewById(R.id.departments_number);

            call_images.setImageResource(call_logo[position]);
            department_titles.setText(departments_title[position]);
            department_number.setText(departments_number[position]);

            return departments;
        }
    }


}
