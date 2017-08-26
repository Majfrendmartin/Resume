package com.wec.resume.view;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
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

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.wec.resume.R;
import com.wec.resume.injection.component.DaggerActivityComponent;
import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.model.BaseItem;
import com.wec.resume.model.Education;
import com.wec.resume.model.Job;
import com.wec.resume.model.ModalPair;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.Skill;
import com.wec.resume.presenter.DetailsActivityFragmentPresenter;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.text.TextUtils.isEmpty;
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

    private static final int PROGRESS_BAR_LEVEL_MULTIPLIER = 20;
    private static final float MONTHS_COUNT = 12f;
    private static final SimpleDateFormat MONTH_YEAR_DATE_FORMATTER = new SimpleDateFormat("MMMM yyyy", Locale.UK);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    @BindView(R.id.rv_items)
    RecyclerView rvItems;

    private ItemsAdapter adapter;

    {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

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
    public void showList(@NonNull List<ModalPair<BaseItem, Boolean>> items, int type) {
        adapter.updateItems(items, type);
    }

    @Override
    public void showItemDetails(int position, boolean visibility) {
        adapter.changeItemDetailsVisibility(position, visibility);
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
            ButterKnife.bind(this, view);
        }

        void setupImage(String url) {
            if (ivItemImage != null) {
                ViewUtils.loadImageToView(getContext(), ivItemImage, url);
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

        @BindView(R.id.tv_dots)
        TextView tvDots;

        @BindView(R.id.layout_details)
        ViewGroup layoutDetails;

        JobHolder(View view) {
            super(view);
        }
    }

    class EducationHolder extends ViewHolder {

        @BindView(R.id.tv_school)
        TextView tvSchool;

        @BindView(R.id.tv_period)
        TextView tvPeriod;

        @BindView(R.id.tv_speciality)
        TextView tvSpeciality;

        @BindView(R.id.ll_speciality)
        ViewGroup llSpeciality;

        EducationHolder(View view) {
            super(view);
        }
    }

    class SkillHolder extends ViewHolder {

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.pb_level)
        ProgressBar pbLevel;

        SkillHolder(View view) {
            super(view);
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<DetailsActivityFragment.ViewHolder> {
        private final List<ModalPair<BaseItem, Boolean>> items = new CopyOnWriteArrayList<>();
        private PublishSubject<Integer> onClickSubject = PublishSubject.create();
        private int type;

        void updateItems(List<ModalPair<BaseItem, Boolean>> items, int type) {
            this.items.clear();
            this.items.addAll(items);
            this.type = type;
            notifyDataSetChanged();
        }

        void changeItemDetailsVisibility(int position, boolean visibility) {
            final JobHolder jobHolder = (JobHolder) rvItems.findViewHolderForAdapterPosition(position);
            TransitionManager.beginDelayedTransition(rvItems);
            jobHolder.layoutDetails.setVisibility(visibility ? VISIBLE : GONE);
            jobHolder.tvDots.setVisibility(!visibility ? VISIBLE : GONE);
        }

        Observable<Integer> getClickedItem() {
            return onClickSubject;
        }

        @Override
        public DetailsActivityFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.default_item, parent, false);

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
            final BaseItem baseItem = items.get(position).first;
            holder.tvTitle.setText(baseItem.getTitle());

            final int itemViewType = holder.getItemViewType();
            if (itemViewType == EDUCATION.ordinal()) {
                bindEducationHolder((EducationHolder) holder, (Education) baseItem);
            } else if (itemViewType == JOBS.ordinal()) {
                bindJobHolder((JobHolder) holder, (Job) baseItem, items.get(position).second, position);
            } else if (itemViewType == SKILLS.ordinal()) {
                bindSkillHolder((SkillHolder) holder, (Skill) baseItem);
            }
        }

        private void bindSkillHolder(SkillHolder skillHolder, Skill skill) {
            final String description = skill.getDescription();
            if (isEmpty(description)) {
                skillHolder.tvDescription.setVisibility(GONE);
            } else {
                skillHolder.tvDescription.setVisibility(VISIBLE);
                skillHolder.tvDescription.setText(description);
            }

            final int skillLevel = skill.getLevel();
            skillHolder.pbLevel.setVisibility(skillLevel > 0 ? VISIBLE : GONE);

            ObjectAnimator.ofInt(skillHolder.pbLevel, "progress", 0, skillLevel * PROGRESS_BAR_LEVEL_MULTIPLIER)
                    .setDuration(1000)
                    .start();
        }

        private void bindEducationHolder(EducationHolder educationHolder, Education education) {
            educationHolder.tvSchool.setText(getString(R.string.school_placeholder,
                    education.getSchool(), education.getFaculty()));

            educationHolder.tvPeriod.setText(getString(R.string.date_placeholder,
                    Integer.toString(education.getStartYear()),
                    Integer.toString(education.getEndYear())));

            final String speciality = education.getSpeciality();
            if (isEmpty(speciality)) {
                educationHolder.llSpeciality.setVisibility(GONE);
            } else {
                educationHolder.llSpeciality.setVisibility(VISIBLE);
                educationHolder.tvSpeciality.setText(speciality);
            }

            educationHolder.setupImage(education.getCover());
        }

        private void bindJobHolder(JobHolder jobHolder, Job job, Boolean detailsVisibility, final int position) {
            jobHolder.tvPosition.setText(job.getPositionName());

            final Date startDate = job.getStartDate();
            final Date endDate = job.getEndDate();

            final String dateString = job.isCurrent() ?
                    getString(R.string.date_placeholder_now, MONTH_YEAR_DATE_FORMATTER.format(startDate)) :
                    getString(R.string.date_placeholder, MONTH_YEAR_DATE_FORMATTER.format(startDate),
                            MONTH_YEAR_DATE_FORMATTER.format(endDate));

            jobHolder.tvPeriod.setText(dateString);

            jobHolder.tvResponsibilities.setText(TextUtils.join("\n",
                    Stream.of(job.getResponsibilities())
                            .map(s -> getString(R.string.list_placeholder, s))
                            .collect(Collectors.toList())));

            final int months = calculateMonthsBetween(startDate, job.isCurrent() ? new Date() : endDate);

            if (months >= MONTHS_COUNT) {
                final float years = months / MONTHS_COUNT;
                jobHolder.tvTime.setText(DECIMAL_FORMAT.format(years));
                jobHolder.tvTimeUnit.setText(getResources().getQuantityString(R.plurals.plural_year, Math.round(years)));
            } else {
                jobHolder.tvTime.setText(Integer.toString(months));
                jobHolder.tvTimeUnit.setText(getResources().getQuantityString(R.plurals.plural_month, months));
            }

            jobHolder.cvContent.setOnClickListener(v -> onClickSubject.onNext(position));

            jobHolder.layoutDetails.setVisibility(detailsVisibility ? VISIBLE : GONE);
            jobHolder.tvDots.setVisibility(!detailsVisibility ? VISIBLE : GONE);

            jobHolder.setupImage(job.getCover());
        }

        private int calculateMonthsBetween(Date startDate, Date endDate) {
            final Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            final Calendar endCalendar = new GregorianCalendar();
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
