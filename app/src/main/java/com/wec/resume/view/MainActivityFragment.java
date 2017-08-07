package com.wec.resume.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerMainActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.BaseResumeItem;
import com.wec.resume.presenter.MainActivityFragmentPresenter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends AbstractPresenterFragment<MainActivityFragmentPresenter>
        implements MainActivityFragmentView {

    public static final String KEY_EXTRA_SELECTED_TYPE = "KEY_EXTRA_SELECTED_TYPE";

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
        sectionsAdapter.getClickedItem()
                .map(BaseResumeItem::getType)
                .subscribe(type -> {
                    presenter.onSectionClicked(type);
                });
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
    public void showList(@NonNull Collection<BaseResumeItem> sections) {
        sectionsAdapter.updateItems(sections);
    }

    @Override
    public void navigateToDetails(@NonNull BaseResumeItem.ResumeItemType type) {
        final Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra(KEY_EXTRA_SELECTED_TYPE, type);
        startActivity(intent);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.iv_item_image)
        ImageView ivItemImage;

        @BindView(R.id.cv_content)
        CardView cvContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setupImage(String url) {
            Glide.with(getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemImage);
        }
    }

    private class SectionsAdapter extends RecyclerView.Adapter<ViewHolder> {

        private PublishSubject<BaseResumeItem> onClickSubject = PublishSubject.create();

        private final List<BaseResumeItem> items = new CopyOnWriteArrayList<>();

        void updateItems(Collection<BaseResumeItem> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        Observable<BaseResumeItem> getClickedItem() {
            return onClickSubject;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final BaseResumeItem resumeItem = items.get(position);
            holder.tvTitle.setText(resumeItem.getTitle());
            holder.setupImage(resumeItem.getCover());
            holder.cvContent.setOnClickListener(v -> {
                onClickSubject.onNext(resumeItem);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
