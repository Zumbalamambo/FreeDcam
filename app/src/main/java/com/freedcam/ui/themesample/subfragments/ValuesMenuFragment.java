/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package com.freedcam.ui.themesample.subfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.freedcam.ui.themesample.subfragments.Interfaces.I_CloseNotice;
import com.troop.freedcam.R.id;
import com.troop.freedcam.R.layout;

/**
 * Created by troop on 15.06.2015.
 */
public class ValuesMenuFragment extends Fragment implements ListView.OnItemClickListener
{
    private String[] item;
    private ListView listView;
    View view;
    private I_CloseNotice i_closeNotice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layout.valuesmenufragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(id.values_fragment_listview);

        if(item == null)
            return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                layout.listviewlayout, id.listviewlayout_textview, item);
        //attach adapter to the listview and fill
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void SetMenuItem(String[] item, I_CloseNotice i_closeNotice)
    {
        this.item = item;
        this.i_closeNotice = i_closeNotice;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) listView.getItemAtPosition(position);
        i_closeNotice.onClose(value);
    }
}
