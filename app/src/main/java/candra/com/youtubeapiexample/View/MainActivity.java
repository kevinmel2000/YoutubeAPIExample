package candra.com.youtubeapiexample.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import candra.com.youtubeapiexample.Model.Item;
import candra.com.youtubeapiexample.Model.Videos;
import candra.com.youtubeapiexample.Presenter.YoutubePresenter;
import candra.com.youtubeapiexample.R;
import candra.com.youtubeapiexample.Utils.Adapter;
import candra.com.youtubeapiexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Observer{

    final String CHANNELID = "UC3PGvKpN1NR-hUp7ppTFWGg";
    ActivityMainBinding binding;
    YoutubePresenter youtubePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolbar();
        youtubePresenter = new YoutubePresenter(this);
        youtubePresenter.addObserver(this);
        youtubePresenter.getVideos(CHANNELID);
    }

    void setToolbar(){
        setSupportActionBar(binding.toolbar);
    }

    void setListVideos(List<Item> items){
        binding.progressbar.setVisibility(View.GONE);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        Adapter<Item, ListViewHolder> adapter;

        adapter = new Adapter<Item, ListViewHolder>(R.layout.list_video, ListViewHolder.class, Item.class, items) {
            @Override
            protected void bindView(ListViewHolder holder, Item model, int position) {
                holder.onBind(MainActivity.this, model);
            }
        };

        binding.list.setLayoutManager(manager);
        binding.list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        youtubePresenter.deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object o) {

        if (o == null){
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            return;
        }

        Videos videos = (Videos) o;
        setListVideos(videos.getItems());
    }
}
