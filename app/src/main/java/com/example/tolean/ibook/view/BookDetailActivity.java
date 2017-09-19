package com.example.tolean.ibook.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.tolean.ibook.R;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.bean.TagsBean;
import com.example.tolean.ibook.presenter.BookPresenter;
import com.example.tolean.ibook.presenter.DatebasePresenter;
import com.example.tolean.ibook.utils.GsonUtil;
import com.example.tolean.ibook.utils.TextUtil;
import com.example.tolean.ibook.viewinterface.IGetBookDetailView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookDetailActivity extends BaseActivity implements IGetBookDetailView {
    @BindView(R.id.wait_toolbar)
    Toolbar mWaitToolbar;
    @BindView(R.id.img_error_layout)
    ImageView mImgErrorLayout;
    @BindView(R.id.loading_progressbar)
    ProgressBar mLoadingProgressbar;
    @BindView(R.id.tv_error)
    TextView mTvError;
    @BindView(R.id.page_layout)
    RelativeLayout mPageLayout;
    @BindView(R.id.layout_wait)
    CoordinatorLayout mLayoutWait;
    @BindView(R.id.bookimage_detail)
    ImageView mBookimageDetail;
    @BindView(R.id.toolbar_detail)
    Toolbar mToolbarDetail;
    @BindView(R.id.coll_toolbar_detail)
    CollapsingToolbarLayout mCollToolbarDetail;
    @BindView(R.id.detail_title)
    TextView mDetailTitle;
    @BindView(R.id.detail_author)
    TextView mDetailAuthor;
    @BindView(R.id.detail_publisher)
    TextView mDetailPublisher;
    @BindView(R.id.detail_publish_time)
    TextView mDetailPublishTime;
    @BindView(R.id.detail_page_and_price)
    TextView mDetailPageAndPrice;
    @BindView(R.id.detail_rating_number)
    TextView mDetailRatingNumber;
    @BindView(R.id.detail_rating)
    RatingBar mDetailRating;
    @BindView(R.id.detail_rating_card)
    CardView mDetailRatingCard;
    @BindView(R.id.layout_detail)
    CoordinatorLayout mLayoutDetail;
    @BindView(R.id.detail_number_raters)
    TextView mDetailTatingNumberRaters;
    @BindView(R.id.detail_appbarlayout)
    AppBarLayout mDetailAppBarLayout;
    @BindView(R.id.detail_about_author)
    TextView mDetailAboutAuthor;
    @BindView(R.id.more_shrink_content_author_summary)
    TextView mMoreShrinkContentAuthorSummary;
    @BindView(R.id.spread_author_summary)
    ImageView mSpreadAuthorSummary;
    @BindView(R.id.shrink_author_summary)
    ImageView mShrinkAuthorSummary;
    @BindView(R.id.show_more_author_summary)
    RelativeLayout mShowMoreAuthorSummary;
    @BindView(R.id.book_detail_author)
    LinearLayout mBookDetailAuthor;
    @BindView(R.id.detail_summary)
    TextView mDetailSummary;
    @BindView(R.id.more_shrink_content_summary)
    TextView mMoreShrinkContentSummary;
    @BindView(R.id.spread_summary)
    ImageView mSpreadSummary;
    @BindView(R.id.shrink_summary)
    ImageView mShrinkSummary;
    @BindView(R.id.show_more_summary)
    RelativeLayout mShowMoreSummary;
    @BindView(R.id.book_detail_summary)
    LinearLayout mBookDetailSummary;
    @BindView(R.id.detail_catalog)
    TextView mDetailCatalog;
    @BindView(R.id.more_shrink_content_catalog)
    TextView mMoreShrinkContentCatalog;
    @BindView(R.id.spread_catalog)
    ImageView mSpreadCatalog;
    @BindView(R.id.shrink_catalog)
    ImageView mShrinkCatalog;
    @BindView(R.id.show_more_catalog)
    RelativeLayout mShowMoreCatalog;
    @BindView(R.id.book_detail_catalog)
    LinearLayout mBookDetailCatalog;
    @BindView(R.id.id_tagflowlayout)
    TagFlowLayout mIdTagflowlayout;
    @BindView(R.id.collect)
    ImageView mCollect;
    private boolean isCollect;
    private BookPresenter mBookPresenter = null;
    private Book mBook=null;
    private String mBookTitle = "";
    private String mBookId;
    private String mBookibsn;
    private static final int MAXLINES_OF_ABOUT_AUTHOR = 4;
    private int MODE = -1;
    public static final int MODE_ID = 0;
    public static final int MODE_IBSN = 1;
    private Unbinder mUnbinder = null;
    private DatebasePresenter mDatebasePresenter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent_ = getIntent();
        MODE = intent_.getIntExtra("mode", -1);
        if (MODE == 0) {
            mBookId = intent_.getStringExtra("book_id");
            mBook = GsonUtil.gsonToBook(intent_.getStringExtra("book_json"));
        } else {
            mBookibsn = intent_.getStringExtra("book_ibsn");
        }
        mDatebasePresenter=DatebasePresenter.getSingletonPresenter(this);
        initView();
        initThem();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        if(isCollect){
                DatebasePresenter.getSingletonPresenter(BookDetailActivity.this).saveInDatebase(mBook);
        }else{
                DatebasePresenter.getSingletonPresenter(BookDetailActivity.this).deleteInDatebase(mBook);
        }

        super.onDestroy();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_book_detail);
        mUnbinder = ButterKnife.bind(this);
        mWaitToolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        mToolbarDetail.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        setLisenter();
        mImgErrorLayout.setImageResource(R.drawable.ic_book_subjectcover_default);
        mImgErrorLayout.setVisibility(View.VISIBLE);
        mLayoutDetail.setVisibility(View.GONE);
        mLoadingProgressbar.setVisibility(View.GONE);
        mLayoutWait.setVisibility(View.VISIBLE);
        if (MODE == MODE_ID) {
//            BookPresenter.getSingletonPresenter().getBooksById(this, mBookId);
            onSuccess(mBook);
        } else {
            BookPresenter.getSingletonPresenter().getBookByIbsn(this, mBookibsn);
        }
    }

    @Override
    protected void initThem() {
//        mToolbarDetail.setBackgroundColor(ThemUtil.getThemColor());
//        applyKitKatTranslucency(ThemUtil.getThemColor());
    }

    @Override
    protected void handleThemChange() {
//        mToolbarDetail.setBackgroundColor(ThemUtil.getThemColor());
//        applyKitKatTranslucency(ThemUtil.getThemColor());
    }

    private void setLisenter() {
        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollect=!isCollect;
                if (isCollect){
                    mCollect.setImageResource(R.drawable.btn_star_big_on);
                }else{
                    mCollect.setImageResource(R.drawable.btn_star_big_off);
                }
//                DatebasePresenter.getSingletonPresenter(BookDetailActivity.this).saveInDatebase(mBook);
//                Log.i("Lyon",String.valueOf(DatebasePresenter.getSingletonPresenter(BookDetailActivity.this).judgeIsCollect(mBook)));
            }
        });
        mDetailAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //这里不知道为什么.  第二次进入时  title的位置会发生偏移
//                    mCollToolbarDetail.setTitle("测试");
                    mToolbarDetail.setTitle(mBookTitle);
                    isShow = true;
                } else if (isShow) {
//                    mCollToolbarDetail.setTitle(" ");
                    mToolbarDetail.setTitle(" ");
                    isShow = false;
                }
            }
        });
        mWaitToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //模拟返回键.否则没有元素共享效果
//                onKeyDown(KeyEvent.KEYCODE_BACK, null);
//                BookDetailActivity.super.onBackPressed();
//                finishAfterTransition();
//                supportFinishAfterTransition();
            }
        });
        mLayoutWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        mToolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSpreadAuthorSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailAboutAuthor.setMaxLines(Integer.MAX_VALUE);
                mMoreShrinkContentAuthorSummary.setText("收起");
                mShrinkAuthorSummary.setVisibility(View.VISIBLE);
                mSpreadAuthorSummary.setVisibility(View.GONE);
            }
        });
        mShrinkAuthorSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailAboutAuthor.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mMoreShrinkContentAuthorSummary.setText("显示更多");
                mShrinkAuthorSummary.setVisibility(View.GONE);
                mSpreadAuthorSummary.setVisibility(View.VISIBLE);
            }
        });
        mSpreadSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailSummary.setMaxLines(Integer.MAX_VALUE);
                mMoreShrinkContentSummary.setText("收起");
                mShrinkSummary.setVisibility(View.VISIBLE);
                mSpreadSummary.setVisibility(View.GONE);
            }
        });
        mShrinkSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailSummary.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mMoreShrinkContentSummary.setText("显示更多");
                mShrinkSummary.setVisibility(View.GONE);
                mSpreadSummary.setVisibility(View.VISIBLE);
            }
        });
        mShrinkCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailCatalog.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mMoreShrinkContentCatalog.setText("显示更多");
                mShrinkCatalog.setVisibility(View.GONE);
                mSpreadCatalog.setVisibility(View.VISIBLE);
            }
        });
        mSpreadCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailCatalog.setMaxLines(Integer.MAX_VALUE);
                mMoreShrinkContentCatalog.setText("收起");
                mShrinkCatalog.setVisibility(View.VISIBLE);
                mSpreadCatalog.setVisibility(View.GONE);
            }
        });
    }

    private void refresh() {
        BookPresenter.getSingletonPresenter().getBooksById(this, mBookId);
    }

    @Override
    public void onSuccess(Book book) {
        if(mBook==null){
            mBook=book;
        }
        isCollect=mDatebasePresenter.judgeIsCollect(book);
        if (isCollect){
            mCollect.setImageResource(R.drawable.btn_star_big_on);
        }else{
            mCollect.setImageResource(R.drawable.btn_star_big_off);
        }
        mBookTitle = book.getTitle();
        Glide.with(this).load(book.getImages().getLarge()).placeholder(R.drawable.ic_book_subjectcover_default).into(mBookimageDetail);
        Glide.with(this).load(book.getImage()).asBitmap().placeholder(R.drawable.ic_book_subjectcover_default).into(target);
        mDetailTitle.setText(book.getTitle());
        for (String author : book.getAuthor()) {
            mDetailAuthor.append(author + " ");
        }
        mDetailPublisher.append(book.getPublisher());
        mDetailPublishTime.append(book.getPubdate());
        mDetailPageAndPrice.setText("页数:" + book.getPages() + " 价格" + book.getPrice());
        mDetailRatingNumber.setText(book.getRating().getAverage() + "分");
        mDetailRating.setRating(Float.parseFloat(book.getRating().getAverage()) / 2);
        mDetailTatingNumberRaters.setText(String.valueOf(book.getRating().getNumRaters()) + "人");

        mIdTagflowlayout.setAdapter(new TagAdapter<TagsBean>(book.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, TagsBean tagsBean) {
                TextView view_ = new TextView(BookDetailActivity.this);
                view_.setText(tagsBean.getTitle());
                view_.setTextColor(getResources().getColor(R.color.brown));
                view_.setBackgroundColor(getResources().getColor(R.color.white));
                view_.setTextSize(12);
                view_.setTypeface(Typeface.SERIF);
                return view_;
            }


        });
        setAuthorSummaryCatalog(book);
        mLayoutDetail.setVisibility(View.VISIBLE);
        mLayoutWait.setVisibility(View.GONE);
    }

    public void setAuthorSummaryCatalog(Book book) {
        if (!TextUtil.isEmpty(book.getAuthor_intro())) {
            mBookDetailAuthor.setVisibility(View.VISIBLE);
            mDetailAboutAuthor.setText(book.getAuthor_intro());
            if (book.getAuthor_intro().length() > 100) {
                mDetailAboutAuthor.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mShowMoreAuthorSummary.setVisibility(View.VISIBLE);
            }
        }
        if (!TextUtil.isEmpty(book.getCatalog())) {
            mDetailCatalog.setText(book.getCatalog());
            mBookDetailCatalog.setVisibility(View.VISIBLE);
            if (book.getCatalog().length() > 100) {
                mDetailCatalog.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mShowMoreCatalog.setVisibility(View.VISIBLE);
            }
        }
        if (!TextUtil.isEmpty(book.getSummary())) {
            mBookDetailSummary.setVisibility(View.VISIBLE);
            mDetailSummary.setText(book.getSummary());
            if (book.getSummary().length() > 100) {
                mDetailSummary.setMaxLines(MAXLINES_OF_ABOUT_AUTHOR);
                mShowMoreSummary.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFailure() {
        mLayoutWait.setVisibility(View.VISIBLE);
        mLayoutDetail.setVisibility(View.GONE);
    }

    protected SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            int defaultColor = ContextCompat.getColor(BookDetailActivity.this, R.color.colorPrimary);
            Palette palette = Palette.from(bitmap).generate();
            int color = palette.getDominantColor(defaultColor);
            int i = 0;
            while (color == defaultColor && i <= 5) {
                switch (i) {
                    case 0:
                        color = palette.getMutedColor(defaultColor);
                        i++;
                        break;
                    case 1:
                        color = palette.getVibrantColor(defaultColor);
                        i++;
                        break;
                    case 2:
                        color = palette.getDarkMutedColor(defaultColor);
                        i++;
                        break;
                    case 3:
                        color = palette.getDarkMutedColor(defaultColor);
                        i++;
                        break;
                    case 4:
                        color = palette.getLightMutedColor(defaultColor);
                        i++;
                        break;
                    case 5:
                        color = palette.getLightVibrantColor(defaultColor);
                        i++;
                        break;
                    default:
                        break;
                }
            }
            mCollToolbarDetail.setContentScrimColor(color);
            mCollToolbarDetail.setBackgroundColor(color);
            mCollToolbarDetail.setStatusBarScrimColor(color);
//            StatusBarUtil.setTransparentForImageView(mContext,bookImageView);
        }
    };

}