package layout;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.octopus.ugmscholar2.AdminSqliteHelper;
import com.octopus.ugmscholar2.InfoDetailsActivity;
import com.octopus.ugmscholar2.ItemData;
import com.octopus.ugmscholar2.R;
import com.octopus.ugmscholar2.RvAdapter;
import com.twotoasters.jazzylistview.effects.FadeEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkedFragment extends Fragment {
    RecyclerView rv;
    RvAdapter rvAdapter;
    private List<ItemData> itemDatas;

    public BookmarkedFragment() {
        // Required empty public constructor
    }

    MyReceiver r;
    public void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BookmarkedFragment.this.refresh();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarked, container, false);



        AdminSqliteHelper sqlite = new AdminSqliteHelper(this.getContext(), "management", null, 1);
        itemDatas = new ArrayList<ItemData>();
        ItemData item;

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        rvAdapter = new RvAdapter(itemDatas, new RvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemData item) {
                Intent intent = new Intent(BookmarkedFragment.this.getContext(), InfoDetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("author", item.getAuthor());
                intent.putExtra("date", item.getTgl());
                intent.putExtra("img", item.getImgUrl());
                intent.putExtra("url",item.getDirectUrl());
                startActivity(intent);
            }
        });
        rv.setAdapter(rvAdapter);
        JazzyRecyclerViewScrollListener jrv = new JazzyRecyclerViewScrollListener();
        jrv.setTransitionEffect(new FadeEffect());
        rv.addOnScrollListener(jrv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);

        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor row = db.rawQuery("SELECT * FROM bookmark ORDER BY id desc", null);
        if(row.moveToFirst()) {
            do{
                item = new ItemData();
                item.setTitle(row.getString(1));
                item.setImgUrl(row.getString(2));
                item.setTgl(row.getString(3));
                item.setAuthor(row.getString(4));
                item.setDirectUrl(row.getString(5));
                itemDatas.add(item);
                rvAdapter.notifyDataSetChanged();

            }while(row.moveToNext());
        }
        db.close();
        return view;
    }


}
