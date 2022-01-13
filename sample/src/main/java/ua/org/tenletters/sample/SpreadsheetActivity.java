package ua.org.tenletters.sample;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ua.org.tenletters.widget.DiagonalScrollView;

public class SpreadsheetActivity extends AppCompatActivity {

    private static int NUM_COLUMNS = 10;
    private static int NUM_ROWS = 30;

    private TableLayout columns;
    private TableLayout rows;
    private TableLayout content;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spreadsheet);

        setupActionBar();

        findViews();

        setupScrollers();

        populateTables();
        
        rePopulateExistingTables();
    }

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
    }

    private void findViews() {
        columns = findViewById(R.id.columns);
        rows = findViewById(R.id.rows);
        content = findViewById(R.id.content);
    }

    /**
     * Sets {@link DiagonalScrollView} to adjust headers on scroll
     */
    // TODO: horizontal and vertical scrollers to move the content table
    private void setupScrollers() {
        final HorizontalScrollView horizontalScroller = findViewById(R.id.horisontal_scroller);
        horizontalScroller.setHorizontalScrollBarEnabled(false);
        horizontalScroller.setOnTouchListener(new TouchConsumer());

        final ScrollView verticalScroller = findViewById(R.id.vertical_scroller);
        verticalScroller.setVerticalScrollBarEnabled(false);
        verticalScroller.setOnTouchListener(new TouchConsumer());

        final DiagonalScrollView diagonalScroller = findViewById(R.id.diagonal_scroller);
        diagonalScroller.addOnScrollListener((left, top, oldLeft, oldTop) -> {
            horizontalScroller.scrollTo(left, 0);
            verticalScroller.scrollTo(0, top);
        });
    }
    
    /**
     *  reset the existing Tables
     */

    private void rePopulateExistingTables() {

        NUM_COLUMNS = 5;
        NUM_ROWS = 4;

        // reset if views are already set
        if (columns.getChildCount()>0) {
            columns.removeAllViews();
        }
        if (rows.getChildCount()>0) {
            rows.removeAllViews();
        }
        if (content.getChildCount()>0) {
            content.removeAllViews();
        }

        populateTables();

    }

    /**
     * Fills "Column" and "Row" headers, and "Content" table with inflated cell views
     */
    private void populateTables() {
        final TableRow topRow = new TableRow(this);
        for (int i = 0; i < NUM_COLUMNS; i++) {
            topRow.addView(getCellView(topRow, getString(R.string.cell_column_format, i),
                    R.color.cell_header, R.color.cell_header_text));
        }
        columns.addView(topRow);

        for (int i = 0; i < NUM_ROWS; i++) {
            final TableRow row = new TableRow(this);
            row.addView(getCellView(row, getString(R.string.cell_row_format, i),
                    R.color.cell_header, R.color.cell_header_text));
            rows.addView(row);
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            final TableRow row = new TableRow(this);
            for (int j = 0; j < NUM_COLUMNS; j++) {
                row.addView(getCellView(row, getString(R.string.cell_content_format, j, i),
                        R.color.cell_content, R.color.cell_content_text));
            }
            content.addView(row);
        }
    }

    /**
     * @return cell view with set text and colors
     */
    private View getCellView(final ViewGroup parent, final String text,
                             @ColorRes final int cellColor, @ColorRes final int textColor) {
        final TextView cellView = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.item_cell, parent, false);

        cellView.setText(text);
        cellView.setBackgroundResource(cellColor);
        cellView.setTextColor(getResources().getColor(textColor));

        return cellView;
    }

    /**
     * This is a Listener that simply blocks all touch events
     */
    private static class TouchConsumer implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
}
