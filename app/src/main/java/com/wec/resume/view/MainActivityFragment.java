package com.wec.resume.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_CENTER;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends AbstractPresenterFragment<MainActivityFragmentPresenter>
        implements MainActivityFragmentView {

    public static final String KEY_EXTRA_SELECTED_TYPE = "KEY_EXTRA_SELECTED_TYPE";

    @BindView(R.id.rv_sections)
    RecyclerView rvSections;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;

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
    public void navigateToDetails(@NonNull SectionType type, int position) {

        final Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(KEY_EXTRA_SELECTED_TYPE, type);

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

    @Override
    public void showHeader(String title, String subtitle) {
        tvTitle.setText(title);
        if (!TextUtils.isEmpty(subtitle)) {
            tvSubtitle.setText(subtitle);
            tvSubtitle.setVisibility(VISIBLE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.iv_item_image)
        ImageView ivItemImage;

        @BindView(R.id.pb_loading)
        View pbLoading;

        @BindView(R.id.cv_content)
        CardView cvContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setupImage(String url, @DrawableRes int placeholderRes) {
            ViewUtils.loadImageToView(getContext(), ivItemImage, url, placeholderRes, new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    ivItemImage.setScaleType(FIT_CENTER);
                    pbLoading.setVisibility(INVISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    ivItemImage.setScaleType(CENTER_CROP);
                    pbLoading.setVisibility(INVISIBLE);
                    return false;
                }
            });
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
            final Section sectionListItem = items.get(position);
            holder.tvTitle.setText(sectionListItem.getTitle());
            holder.setupImage(sectionListItem.getCover(),
                    ViewUtils.getDrawableForSectionType(sectionListItem.getType()));
            holder.cvContent.setOnClickListener(
                    v -> onClickSubject.onNext(Pair.create(sectionListItem.getType(), position)));

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
