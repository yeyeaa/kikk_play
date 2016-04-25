package com.jude.easyrecyclerview.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jude.easyrecyclerview.R;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

	public interface DividerProvider {
		int[] getDividerDrawableIds();
	}

	private static int[] dividerIds = new int[] { R.drawable.hordivider, R.drawable.verdivider };
	private Drawable mHorizontalDivider;
	private Drawable mVerticalDivider;

	public DividerGridItemDecoration(Context context) {
		initDivider(context);
	}

	public DividerGridItemDecoration(Context context, DividerProvider dividerProvider) {
		if (dividerProvider != null && dividerProvider.getDividerDrawableIds() != null
				&& dividerProvider.getDividerDrawableIds().length > 0) {
			dividerIds = dividerProvider.getDividerDrawableIds();
			if (dividerIds.length == 1) {
				dividerIds[1] = dividerIds[0];
			}
		}
		initDivider(context);
	}

	private void initDivider(Context context) {
		mHorizontalDivider = ContextCompat.getDrawable(context, dividerIds[0]);
		mVerticalDivider = ContextCompat.getDrawable(context, dividerIds[1]);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, State state) {
		drawHorizontal(c, parent);
		drawVertical(c, parent);
	}

	private int getSpanCount(RecyclerView parent) {
		int spanCount = -1;
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		}
		return spanCount;
	}

	public void drawHorizontal(Canvas c, RecyclerView parent) {
		int childCount = parent.getChildCount();
		int spanCount = getSpanCount(parent);
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getLeft() - params.leftMargin;
			final int right = child.getRight() + params.rightMargin + mVerticalDivider.getIntrinsicWidth();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mHorizontalDivider.getIntrinsicHeight();
			mHorizontalDivider.setBounds(left, top, right, bottom);
			mHorizontalDivider.draw(c);
		}
	}

	public void drawVertical(Canvas c, RecyclerView parent) {
		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);

			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getTop() - params.topMargin;
			final int bottom = child.getBottom() + params.bottomMargin;
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mVerticalDivider.getIntrinsicWidth();

			mVerticalDivider.setBounds(left, top, right, bottom);
			mVerticalDivider.draw(c);
		}
	}

	private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			if ((pos + 1) % spanCount == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			int temp = 0;
			if (childCount % spanCount == 0) {
				temp = spanCount;
			}
			childCount = childCount - childCount % spanCount;
			childCount -= temp;
			if (pos >= childCount)
				return true;
		}
		return false;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
		if ("extraView".equals(view.getTag())) {
			return;
		}
		int spanCount = getSpanCount(parent);
		int childCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
		int itemPosition = view.getId();

		if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
			outRect.set(0, 0, mHorizontalDivider.getIntrinsicWidth(), 0);
		} else {
			outRect.set(0, 0, mHorizontalDivider.getIntrinsicWidth(), mVerticalDivider.getIntrinsicHeight());
		}
	}
}