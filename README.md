DiagonalScrollView
==================

The DiagonalScrollView is based on somewhat outdated version of AOSP HorizontalScrollView. It supports diagonal fling gesture, saves and restores it's scroll position, but doesn't support RTL layouts. Other limitations are basically the same as for the HorizontalScrollView.

You can declare it in XML:
```xml
<ua.org.tenletters.widget.DiagonalScrollView
    android:id="@+id/diagonal_scroller"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <TableLayout
        android:id="@+id/content"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:stretchColumns="1"
        />
</ua.org.tenletters.widget.DiagonalScrollView>
```

You can add a listener in your Java code:
```java
diagonalScroller.addOnScrollListener((left, top, oldLeft, oldTop) -> {
    // Some code
});
```

This library doesn't bring other dependencies. It's footprint is 11,9 KB, 70 defined methods.
