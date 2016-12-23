package com.rudy.framework.module.discovery.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.rudy.framework.R;
import com.rudy.framework.base.adapter.CommonAdapter;
import com.rudy.framework.base.adapter.DividerItemDecoration;
import com.rudy.framework.base.presenter.BasePresenter;
import com.rudy.framework.base.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RudyJun on 2016/12/22.
 */

public class DiscoveryFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private CommonAdapter<String> commonAdapter;
    private List<String> stringList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置下拉刷新圈圈颜色变化
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //设置RecyclerView的布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //给RecyclerView设置分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        initData();
        //初始化适配器
        commonAdapter = new CommonAdapter<String>(R.layout.item_simple, stringList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tvContent, item)
                        .addOnClickListener(R.id.ivAvatar)
                        .addOnClickListener(R.id.tvContent)
                        .addOnLongClickListener(R.id.ivAvatar)
                        .addOnLongClickListener(R.id.tvContent);
            }
        };

        mRecyclerView.setAdapter(commonAdapter);
        commonAdapter.setOnLoadMoreListener(this);

        //设置Item和ItemChild的点击事件和长按事件
        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(activity, "点击了Item ：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(activity, "长按了Item ：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ivAvatar:
                        Toast.makeText(activity, "点击了头像", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.tvContent:
                        Toast.makeText(activity, "点击了内容", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ivAvatar:
                        Toast.makeText(activity, "长按了头像 ：" + position, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.tvContent:
                        Toast.makeText(activity, "长按了内容 ：" + position, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

    }

    private void initData() {
        stringList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            stringList.add("数据 : " + i);
        }
    }

    @Override
    public void fetchData() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onRefresh() {
        //刷新时，不允许加载更多
        commonAdapter.setEnableLoadMore(false);
        initData();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonAdapter.setNewData(stringList);
                mSwipeRefreshLayout.setRefreshing(false);
                commonAdapter.setEnableLoadMore(true);
            }
        }, 2000);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载更多时，不允许刷新
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonAdapter.addData("更多数据");
                commonAdapter.loadMoreComplete();
                mSwipeRefreshLayout.setEnabled(true);
            }
        }, 2000);
    }
}
