package com.veyxstudio.shulehu.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.veyxstudio.shulehu.R;
import com.veyxstudio.shulehu.TagArticleListActivity;
import com.veyxstudio.shulehu.object.ArticleList;
import com.veyxstudio.shulehu.util.KeyWordHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Created by Veyx Shaw on 2016/2/22.
 * Handle article.
 */
public class TagArticleListHandler extends Handler{
    private static final String LOG_TAG = "TagArticleListHandler";

    public TagArticleListHandler(TagArticleListActivity currentActivity){
        activityReference = new WeakReference<>(currentActivity);
    }


    private WeakReference<TagArticleListActivity> activityReference;

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == KeyWordHelper.LOAD_OK) {
            TagArticleListActivity activity = activityReference.get();
            Toolbar toolbar = (Toolbar)activity.findViewById(R.id.article_list_toolbar);
            toolbar.setTitle(activity.getArticleListTitle());
            generateList();
            activity.clearBusy();activity.closeSnackBar();
        } else if (msg.what == KeyWordHelper.STATE_OUT) {
            Toast.makeText(activityReference.get(),
                    R.string.no_SHUPassport,
                    Toast.LENGTH_SHORT).show();
            TagArticleListActivity activity = activityReference.get();
            activity.clearBusy();activity.closeSnackBar();
        } else if (msg.what == KeyWordHelper.NO_NETWORK) {
            Toast.makeText(activityReference.get(), R.string.no_network, Toast.LENGTH_SHORT).show();
            TagArticleListActivity activity = activityReference.get();
            activity.clearBusy();activity.closeSnackBar();
        }
    }

    private void generateList(){
        TagArticleListActivity activity = activityReference.get();
        // Generate Article List
        Log.i(LOG_TAG, "Generate Article List");
        ListView listView = (ListView)activity.findViewById(R.id.article_list_list);
        SimpleAdapter adapter;
        String[] keys = {"Title", "Date", /*"Content", "Signature"*/};
        int[] ids = {R.id.article_list_title, R.id.article_list_date,
                /*R.id.article_content , R.id.article_signature*/};
        ListIterator<ArticleList> iterator = activity.getArticleListList().listIterator();
        ArrayList<HashMap<String,String>> listItem = new ArrayList<>();
        int i=0;
        while (iterator.hasNext()){
            HashMap<String, String> map = new HashMap<>();
            ArticleList articleList = iterator.next();
            map.put(keys[0], articleList.getTitle());
            map.put(keys[1] , articleList.getSource() + " "
                    + articleList.getDate() + " ");
            // map.put("content" , tradeRecord.getMoney());
            //map.put("Signature" , article.getSignature());
            listItem.add(map);
            i++;
            activity.setAids(articleList.getAid(), i);
        }
        Log.i(LOG_TAG, "Article List Count:"+i);
        adapter = new SimpleAdapter(activity,
                listItem, R.layout.list_article_list, keys, ids);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(activity);
        if(i==0){
            Toast.makeText(activity, R.string.search_result_empty,Toast.LENGTH_SHORT).show();
        }
    }
}
