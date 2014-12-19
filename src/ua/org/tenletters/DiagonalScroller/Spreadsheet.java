package ua.org.tenletters.DiagonalScroller;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class Spreadsheet extends Activity {

    private TableLayout topLeftTable;

    private TableLayout topTable;
    private HorizontalScrollView horizontalScroller;

    private TableLayout leftTable;
    private ScrollView verticalScroller;

    private TableLayout contentTable;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spreadsheet);

        removeActionBarIcon();

        // Placeholder in corner
        topLeftTable = (TableLayout) findViewById(R.id.topLeftTable);

        // Column titles
        topTable = (TableLayout) findViewById(R.id.topTable);
        horizontalScroller = (HorizontalScrollView) findViewById(R.id.horisontalScroller);
        horizontalScroller.setHorizontalScrollBarEnabled(false);
        horizontalScroller.setOnTouchListener(new TouchConsumer());

        // Row titles
        leftTable = (TableLayout) findViewById(R.id.leftTable);
        verticalScroller = (ScrollView) findViewById(R.id.verticalScroller);
        verticalScroller.setVerticalScrollBarEnabled(false);
        verticalScroller.setOnTouchListener(new TouchConsumer());

        // Cells
        contentTable = (TableLayout) findViewById(R.id.contentTable);
        final DiagonalScrollView diagonalScroller = (DiagonalScrollView) findViewById(R.id.diagonalScrollView);
        diagonalScroller.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
                horizontalScroller.scrollTo(left, 0);
                verticalScroller.scrollTo(0, top);
            }
        });

        populateTables(getTableData());
    }

    /**
     * @return List of String arrays, where on zero position of List is an array of column headers (zero element of
     * this array is empty). Other arrays represent rows (zero element is a row title).
     */
    private List<String[]> getTableData() {
        final List<String[]> data = new ArrayList<>();
        final int columns = 10;
        final int rows = 30;

        final String[] header = new String[columns + 1];
        header[0] = "";
        for (int i = 1; i <= columns; ++i) {
            header[i] = "Column " + (i - 1);
        }
        data.add(header);

        for (int i = 1; i <= rows; ++i) {
            final String[] row = new String[columns + 1];
            row[0] = "Row " + (i - 1);
            for (int j = 1; j <= columns; ++j) {
                row[j] = "Cell " + (j - 1) + "," + (i - 1);
            }
            data.add(row);
        }
        return data;
    }

    private void removeActionBarIcon() {
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
    }

    private void populateTables(final List<String[]> content) {
        TableRow topLeftTableRow = null;
        TableRow topTableRow = null;
        final int columnsTotal = content.get(0).length;
        int maxLeftTableCellWidth = 0;
        int[] maxContentTableColumnWidths = new int[columnsTotal - 1];

        // For each row
        for (int rowCounter = 0; rowCounter < content.size(); ++rowCounter) {
            final TableRow leftTableRow = new TableRow(this);
            leftTableRow.setBackgroundResource(R.color.tableHeaders);
            // Get row title
            final View leftTableCell = getCellView(leftTableRow, content.get(rowCounter)[0], R.color.textHeaders);
            leftTableRow.addView(leftTableCell);
            // Check row title width
            if (leftTableCell.getMeasuredWidth() > maxLeftTableCellWidth) {
                maxLeftTableCellWidth = leftTableCell.getMeasuredWidth();
            }

            // Get rest of the row
            final TableRow contentTableRow = new TableRow(this);
            contentTableRow.setBackgroundResource(R.color.tableRows);

            final boolean currentRowIsHeader = rowCounter == 0;
            for (int columnCounter = 1; columnCounter < columnsTotal; ++columnCounter) {
                final int textColorRes = currentRowIsHeader ? R.color.textHeaders : R.color.textContent;
                final View contentTableCell = getCellView(contentTableRow, content.get(rowCounter)[columnCounter],
                        textColorRes);
                contentTableRow.addView(contentTableCell);

                if (contentTableCell.getMeasuredWidth() > maxContentTableColumnWidths[columnCounter - 1]) {
                    maxContentTableColumnWidths[columnCounter - 1] = contentTableCell.getMeasuredWidth();
                }
            }

            if (currentRowIsHeader) {
                topLeftTableRow = leftTableRow;
                topLeftTableRow.setBackgroundResource(R.color.tableRows);

                topTableRow = contentTableRow;
                topTableRow.setBackgroundResource(R.color.tableHeaders);

                topLeftTable.addView(topLeftTableRow);
                topTable.addView(topTableRow);
            } else {
                leftTable.addView(leftTableRow);
                contentTable.addView(contentTableRow);
            }
        }

        // Set nice width to every column

        final View cornerCell = topLeftTableRow.getChildAt(0);
        cornerCell.setMinimumWidth(maxLeftTableCellWidth);

        setWidthToEachCellInRow(topTableRow, maxContentTableColumnWidths);

        for (int i = 0; i < contentTable.getChildCount(); ++i) {
            final TableRow leftTableRow = (TableRow) leftTable.getChildAt(i);
            final View rowTitleCell = leftTableRow.getChildAt(0);
            rowTitleCell.setMinimumWidth(maxLeftTableCellWidth);

            final TableRow contentTableRow = (TableRow) contentTable.getChildAt(i);
            setWidthToEachCellInRow(contentTableRow, maxContentTableColumnWidths);
        }
    }

    /**
     * @return measured View
     */
    private View getCellView(final ViewGroup parent, final String text, final int colorRes) {
        final View leftTableCell = LayoutInflater.from(this).inflate(R.layout.cell, parent, false);
        final TextView leftTableCellText = (TextView) leftTableCell.findViewById(R.id.itemText);
        leftTableCellText.setText(text);
        leftTableCellText.setTextColor(getResources().getColor(colorRes));

        leftTableCell.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return leftTableCell;
    }

    private void setWidthToEachCellInRow(final TableRow row, int[] widths) {
        if (row != null) {
            for (int i = 0; i < row.getChildCount(); ++i) {
                final View cell = row.getChildAt(i);
                cell.setMinimumWidth(widths[i]);
            }
        }
    }

    private static class TouchConsumer implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
}
