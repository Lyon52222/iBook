package com.example.tolean.ibook.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tolean.ibook.R;
import com.example.tolean.ibook.adapter.CoverFlowAdapter;
import com.example.tolean.ibook.base.BaseActivity;
import com.example.tolean.ibook.bean.Book;
import com.example.tolean.ibook.presenter.DatebasePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class FavoritesActivity extends BaseActivity {

    @BindView(R.id.favorites_toolbar)
    Toolbar mFavoritesToolbar;
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
    @BindView(R.id.detail_number_raters)
    TextView mDetailNumberRaters;
    @BindView(R.id.detail_rating_card)
    CardView mDetailRatingCard;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.no_collection)
    ImageView mNoCollection;

    @BindView(R.id.information)
    LinearLayout mInformation;
    @BindView(R.id.has_collection)
    LinearLayout mHasCollection;
    private CoverFlowAdapter mBaseAdapter;
    @BindView(R.id.coverflow)
    FeatureCoverFlow mCoverflow;
    List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initThem();
    }

    @Override
    protected void onStart() {
        Log.i("Lyon", "ok");
//        mBaseAdapter.refreshdate(DatebasePresenter.getSingletonPresenter(this).getBooks());
//        mCoverflow.setAdapter(mBaseAdapter);
//        mBaseAdapter.notifyDataSetChanged();
//        initView();
        super.onStart();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        mBooks = DatebasePresenter.getSingletonPresenter(this).getBooks();
        if (mBooks.size() != 0) {
            mBaseAdapter = new CoverFlowAdapter(this, mBooks);
            mCoverflow.setAdapter(mBaseAdapter);
            mCoverflow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                @Override
                public void onScrolledToPosition(int position) {
                    mBaseAdapter.setTitle(mFavoritesToolbar, position);

                    Book book = mBaseAdapter.getBook(position);
                    mDetailTitle.setText(book.getTitle());
                    mDetailAuthor.setText("");
                    for (String author : book.getAuthor()) {
                        mDetailAuthor.append(author + " ");
                    }
                    mDetailPublisher.setText("");
                    mDetailPublisher.append(book.getPublisher());
                    mDetailPublishTime.setText("");
                    mDetailPublishTime.append(book.getPubdate());
                    mDetailPageAndPrice.setText("页数:" + book.getPages() + " 价格" + book.getPrice());
                    mDetailRatingNumber.setText(book.getRating().getAverage() + "分");
                    mDetailRating.setRating(Float.parseFloat(book.getRating().getAverage()) / 2);
                    mDetailNumberRaters.setText(String.valueOf(book.getRating().getNumRaters()) + "人");

                    mText.setText(book.getSummary());
                }

                @Override
                public void onScrolling() {

                }
            });
//
            mHasCollection.setVisibility(View.VISIBLE);
        }else{
            mNoCollection.setVisibility(View.VISIBLE);
            Snackbar.make(mNoCollection,"暂时没有收藏,快去找你喜欢的书吧?",Snackbar.LENGTH_LONG).show();
        }
        mFavoritesToolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        mFavoritesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void initThem() {

    }

    @Override
    protected void handleThemChange() {

    }
}
