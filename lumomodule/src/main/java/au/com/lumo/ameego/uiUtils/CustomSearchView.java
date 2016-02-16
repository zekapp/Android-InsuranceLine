package au.com.lumo.ameego.uiUtils;

import android.content.Context;
import android.widget.SearchView;

/**
 * Created by Zeki Guler on 4/08/15.
 */
public class CustomSearchView extends SearchView {

    OnSearchViewCollapsedEventListener mSearchViewCollapsedEventListener;

    public CustomSearchView(Context context) {
        super(context);
    }

    @Override
    public void onActionViewCollapsed() {
        if (mSearchViewCollapsedEventListener != null)
            mSearchViewCollapsedEventListener.onSearchViewCollapsed();
        super.onActionViewCollapsed();
    }

    public interface OnSearchViewCollapsedEventListener {
        public void onSearchViewCollapsed();
    }

    public void setOnSearchViewCollapsedEventListener(OnSearchViewCollapsedEventListener eventListener) {
        mSearchViewCollapsedEventListener = eventListener;
    }

}
