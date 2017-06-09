package top.defaults.kotlinoverflow.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import top.defaults.kotlinoverflow.R;

public class FlowLayout extends ViewGroup {
    private int maxLines;
    private int maxViewCount;
    private int horizontalSpacing;
    private int verticalSpacing;

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            maxLines = a.getInteger(R.styleable.FlowLayout_maxLines, Integer.MAX_VALUE);
            maxViewCount = a.getInteger(R.styleable.FlowLayout_maxViewCount, Integer.MAX_VALUE);
            horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0);
            verticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthLimit = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

        int width = 0;

        int currentWidth = getPaddingLeft();
        int currentHeight = getPaddingTop();

        int maxChildHeight = 0;

        boolean breakLine = false;
        boolean newLine = false;
        int lineCount = 1;
        int spacing = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (i >= maxViewCount) {
                removeViews(i, count - i);
                break;
            }
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            spacing = horizontalSpacing;

            if (lp.horizontalSpacing >= 0) {
                spacing = lp.horizontalSpacing;
            }

            if (growHeight && (breakLine || ((currentWidth + child.getMeasuredWidth()) > widthLimit))) {
                lineCount++;
                if (lineCount > maxLines) {
                    removeViews(i, count - i);
                    break;
                }
                newLine = true;

                currentHeight += maxChildHeight + verticalSpacing;
                width = Math.max(width, currentWidth - spacing);
                currentWidth = getPaddingLeft();
                maxChildHeight = 0;
            } else {
                newLine = false;
            }

            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());

            lp.x = currentWidth;
            lp.y = currentHeight;

            currentWidth += child.getMeasuredWidth() + spacing;

            breakLine = lp.breakLine;
        }

        if (!newLine) {
            width = Math.max(width, currentWidth - spacing);
        }

        width += getPaddingRight();
        int height = currentHeight + maxChildHeight + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;

        int horizontalSpacing;
        boolean breakLine;

        LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
            try {
                horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_Layout_horizontalSpacing, -1);
                breakLine = a.getBoolean(R.styleable.FlowLayout_Layout_breakLine, false);
            } finally {
                a.recycle();
            }
        }

        LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}