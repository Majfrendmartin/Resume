package com.wec.resume.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.BaseItem;
import com.wec.resume.model.Education;
import com.wec.resume.model.Job;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.Skill;
import com.wec.resume.presenter.DetailsActivityFragmentPresenter;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wec.resume.model.Section.SectionType.EDUCATION;
import static com.wec.resume.model.Section.SectionType.JOBS;
import static com.wec.resume.model.Section.SectionType.SKILLS;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends AbstractPresenterFragment<DetailsActivityFragmentPresenter>
        implements DetailsActivityFragmentView {

    private final static SimpleDateFormat MONTH_YEAR_DATE_FORMATTER = new SimpleDateFormat("MMMM yyyy", Locale.UK);
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    public static final float MONTHS_COUNT = 12f;

    {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    @BindView(R.id.rv_items)
    RecyclerView rvItems;

    private ItemsAdapter adapter;

    public DetailsActivityFragment() {
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
        final Intent intent = getActivity().getIntent();
        if (intent != null) {
            final Serializable serializable = intent.getSerializableExtra(MainActivityFragment.KEY_EXTRA_SELECTED_TYPE);
            if (serializable != null) {
                presenter.setType((SectionType) serializable);
            }
        }

        onCreateAfterInjection(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(layoutManager);
        adapter = new ItemsAdapter();
        rvItems.setAdapter(adapter);
        adapter.getClickedItem()
                .subscribe(position -> presenter.onItemClicked(position));
    }


    @Override
    public void showList(@NonNull Collection<BaseItem> items, int type) {
        adapter.updateItems(items, type);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @Nullable
        @BindView(R.id.iv_item_image)
        ImageView ivItemImage;

        @BindView(R.id.cv_content)
        CardView cvContent;

        ViewHolder(View view) {
            super(view);
        }

        void setupImage(String url) {
            if (ivItemImage != null) {
                Glide.with(getActivity())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivItemImage);
            }
        }
    }

    class JobHolder extends ViewHolder {

        @BindView(R.id.tv_position)
        TextView tvPosition;

        @BindView(R.id.tv_period)
        TextView tvPeriod;

        @BindView(R.id.tv_time)
        TextView tvTime;

        @BindView(R.id.tv_time_unit)
        TextView tvTimeUnit;

        @BindView(R.id.tv_responsibilities)
        TextView tvResponsibilities;

        JobHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class EducationHolder extends ViewHolder {

        @BindView(R.id.tv_school)
        TextView tvSchool;

        @BindView(R.id.tv_period)
        TextView tvPeriod;

        EducationHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class SkillHolder extends ViewHolder {

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.pb_level)
        ProgressBar pbLevel;

        SkillHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<DetailsActivityFragment.ViewHolder> {
        private PublishSubject<Integer> onClickSubject = PublishSubject.create();

        private final List<BaseItem> items = new CopyOnWriteArrayList<>();
        private int type;

        void updateItems(Collection<BaseItem> items, int type) {
            this.items.clear();
            this.items.addAll(items);
            this.type = type;
            notifyDataSetChanged();
        }

        Observable<Integer> getClickedItem() {
            return onClickSubject;
        }

        @Override
        public DetailsActivityFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_item, parent, false);

            if (type == EDUCATION.ordinal()) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.education_item, parent, false);
                return new EducationHolder(view);
            } else if (type == JOBS.ordinal()) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.job_item, parent, false);
                return new JobHolder(view);
            } else if (type == SKILLS.ordinal()) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.skill_item, parent, false);
                return new SkillHolder(view);
            }

            return new DetailsActivityFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DetailsActivityFragment.ViewHolder holder, final int position) {
            final BaseItem baseItem = items.get(position);
            holder.tvTitle.setText(baseItem.getTitle());

            final int itemViewType = holder.getItemViewType();
            if (itemViewType == EDUCATION.ordinal()) {
                bindEducationHolder((EducationHolder) holder, (Education) baseItem);
            } else if (itemViewType == JOBS.ordinal()) {
                bindJobHolder((JobHolder) holder, (Job) baseItem);
            } else if (itemViewType == SKILLS.ordinal()) {
                bindSkillHolder((SkillHolder) holder, (Skill) baseItem);
            }
        }

        private void bindSkillHolder(SkillHolder skillHolder, Skill skill) {
            final String description = skill.getDescription();
            if (TextUtils.isEmpty(description)) {
                skillHolder.tvDescription.setVisibility(GONE);
            } else {
                skillHolder.tvDescription.setVisibility(VISIBLE);
                skillHolder.tvDescription.setText(description);
            }

            final int skillLevel = skill.getLevel();
            skillHolder.pbLevel.setVisibility(skillLevel > 0 ? VISIBLE : GONE);
            skillHolder.pbLevel.setProgress(skillLevel);
        }

        private void bindEducationHolder(EducationHolder educationHolder, Education education) {
            educationHolder.tvSchool.setText(getString(R.string.school_placeholder,
                    education.getSchool(), education.getFaculty()));

            educationHolder.tvPeriod.setText(getString(R.string.date_placecholder,
                    Integer.toString(education.getStartYear()),
                    Integer.toString(education.getEndYear())));
        }

        private void bindJobHolder(JobHolder jobHolder, Job job) {
            jobHolder.tvPosition.setText(job.getPositionName());

            final Date startDate = job.getStartDate();
            final Date endDate = job.getEndDate();

            final String dateString = job.isCurrent() ?
                    getString(R.string.date_placecholder_now, MONTH_YEAR_DATE_FORMATTER.format(startDate)) :
                    getString(R.string.date_placecholder, MONTH_YEAR_DATE_FORMATTER.format(startDate),
                            MONTH_YEAR_DATE_FORMATTER.format(endDate));

            jobHolder.tvPeriod.setText(dateString);

            final List<String> responsibilities = job.getResponsibilities();
            final List<String> prefixedItems = new ArrayList<>(responsibilities.size());

            for (String s : responsibilities) {
                prefixedItems.add(getString(R.string.list_placeholder, s));
            }

            jobHolder.tvResponsibilities.setText(TextUtils.join("\n", prefixedItems));

            final int months = calculateMonthsBetween(startDate, job.isCurrent() ? new Date() : endDate);

            if (months >= MONTHS_COUNT) {
                final float years = months / MONTHS_COUNT;
                jobHolder.tvTime.setText(DECIMAL_FORMAT.format(years));
                jobHolder.tvTimeUnit.setText(getResources().getQuantityString(R.plurals.plural_year, Math.round(years)));
            } else {
                jobHolder.tvTime.setText(Integer.toString(months));
                jobHolder.tvTimeUnit.setText(getResources().getQuantityString(R.plurals.plural_year, months));
            }
        }

        private int calculateMonthsBetween(Date startDate, Date endDate) {
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        }

        @Override
        public int getItemViewType(int position) {
            return type;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
