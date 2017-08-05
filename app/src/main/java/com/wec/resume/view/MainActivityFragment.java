package com.wec.resume.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerMainActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.presenter.MainActivityFragmentPresenter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends AbstractPresenterFragment<MainActivityFragmentPresenter>
        implements MainActivityFragmentView {

    @BindView(R.id.rv_sections)
    RecyclerView rvSections;
    private SectionsAdapter sectionsAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        rvSections.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSections.setLayoutManager(layoutManager);
        sectionsAdapter = new SectionsAdapter();
        rvSections.setAdapter(sectionsAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .presenterModule(new PresenterModule())
                .build()
                .inject(this);

        presenter.bindView(this);
        onCreateAfterInjection(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showList(List<String> strings) {
        sectionsAdapter.updateItems(strings);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView itemTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class SectionsAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<String> items = new CopyOnWriteArrayList<>();

        public void updateItems(Collection<String> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemTitle.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
