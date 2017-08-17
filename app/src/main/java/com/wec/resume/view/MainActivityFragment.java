package com.wec.resume.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.Section;
import com.wec.resume.model.Section.SectionType;
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
                .subscribe(pair -> presenter.onSectionClicked(pair.first, pair.second));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .presenterModule(new PresenterModule())
                .build()
                .inject(this);

        presenter.bindView(this);
        onCreateAfterInjection(savedInstanceState);
    }

    @Override
    public void showList(@NonNull Collection<Section> sections) {
        sectionsAdapter.updateItems(sections);
    }

    @Override
    public void navigateToDetails(@NonNull SectionType type, int position, boolean useTransition) {

        final Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(KEY_EXTRA_SELECTED_TYPE, type);

        if (!useTransition) {
            startActivity(intent);
            return;
        }

        final ViewHolder viewHolder = (ViewHolder) rvSections.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            return;
        }

        final ActivityOptionsCompat activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        viewHolder.ivItemImage, getString(R.string.transition_cover));


        ActivityCompat.startActivity(getActivity(), intent, activityOptionsCompat.toBundle());
    }

    @Override
    public void showNoInternetConnectionInfo() {
        Toast.makeText(getContext(), R.string.no_internet_info, Toast.LENGTH_LONG).show();
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
            Glide.with(getActivity())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemImage);
        }
    }

    private class SectionsAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Section> items = new CopyOnWriteArrayList<>();
        private PublishSubject<Pair<SectionType, Integer>> onClickSubject = PublishSubject.create();

        void updateItems(Collection<Section> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        Observable<Pair<SectionType, Integer>> getClickedItem() {
            return onClickSubject;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Section resumeItem = items.get(position);
            holder.tvTitle.setText(resumeItem.getTitle());
            holder.setupImage(resumeItem.getCover());
            holder.cvContent.setOnClickListener(
                    v -> onClickSubject.onNext(Pair.create(resumeItem.getType(), position)));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
