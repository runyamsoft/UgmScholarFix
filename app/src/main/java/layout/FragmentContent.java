package layout;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octopus.ugmscholar2.EndlessRecyclerViewScrollListener;
import com.octopus.ugmscholar2.InfoDetailsActivity;
import com.octopus.ugmscholar2.ItemData;
import com.octopus.ugmscholar2.R;
import com.octopus.ugmscholar2.RvAdapter;
import com.twotoasters.jazzylistview.effects.FadeEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContent extends Fragment {
    private List <ItemData> itemDatas;
    RecyclerView rv;
    int pageCounter;

    public FragmentContent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        pageCounter=1;
        itemDatas = new ArrayList<ItemData>();

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        final RvAdapter rvAdapter = new RvAdapter(itemDatas, new RvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemData item) {
                Intent intent = new Intent(FragmentContent.this.getContext(), InfoDetailsActivity.class);
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

        new ParsePage(itemDatas, rvAdapter).execute();
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new ParsePage(itemDatas, rvAdapter).execute();
            }
        });
        return view;
    }
    private class ParsePage extends AsyncTask<String, Void, Void> {

        List<ItemData> datas;
        ItemData tmpData;
        RvAdapter rvAdapter;
        ParsePage(List<ItemData> datas, RvAdapter rvAdapter){
            this.datas = datas;
            this.rvAdapter = rvAdapter;
        }


        @Override
        protected Void doInBackground(String... params) {
            Document doc;
            Elements articles;

            try {
                int limit = pageCounter +2;

                for(int i = pageCounter; i<=limit; i++) {
                    doc = Jsoup.connect("http://ditmawa.ugm.ac.id/category/info-beasiswa/page/" + i).get();
                    articles = doc.select(".uk-article");


                    for (Element article : articles) {
                        tmpData = new ItemData();
                        tmpData.setTitle(article.select(".uk-article-title").text());
                        tmpData.setAuthor(article.select(".uk-article-meta a").first().text());
                        tmpData.setTgl(article.select(".uk-article-meta time").first().text());
                        tmpData.setDirectUrl(article.select(".uk-article-title a").first().attr("href"));
                        if (article.select("img").size() > 0) {
                            tmpData.setImgUrl("http://ditmawa.ugm.ac.id" + article.select("img").first().attr("src"));
                        } else {
                            tmpData.setImgUrl("");
                        }
                        datas.add(tmpData);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pageCounter+=3;
            rvAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
            Log.d("wa", itemDatas.get(0).getTitle());
        }
    }
}
