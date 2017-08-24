DiagonalScrollView
==================

The DiagonalScrollView is based on somewhat outdated version of AOSP HorizontalScrollView. It supports diagonal fling gesture, saves and restores it's scroll position, but doesn't support RTL layouts. Other limitations are basically the same as for the HorizontalScrollView.

![demo.gif](https://github.com/LissF/DiagonalScrollView/raw/master/demo.gif "Demo")

1) I suppose, you already have jcenter() in your build.gradle. So, just add a dependency:
```
compile "ua.org.tenletters.widget:diagonalscrollview:0.3.3"
```
2) Declare the view in your XML:
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
3) There's no step 3, but you can add a listener in your code:
```java
diagonalScroller.addOnScrollListener((left, top, oldLeft, oldTop) -> {
    // Some code
});
```

This library doesn't bring other dependencies. It's footprint is 11,9 KB, 70 defined methods.
