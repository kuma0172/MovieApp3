package com.example.finalprojects21;
/**
 *@Preeti Kumari
 *@version1.0
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * fragment in its graphical interface to show details about a selected item from the RecyclerView.
 */

public class BuyFragment extends Fragment {

    private Button buy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.buy_fragment, container, false);
        buy = (Button) view.findViewById((R.id.buy_button));
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(BuyFragment.this).commit();
            }
        });
        return view;


    }
}
